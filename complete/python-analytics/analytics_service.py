import json
import time
from confluent_kafka import Consumer, Producer # type: ignore
import pandas as pd # type: ignore
import numpy as np # type: ignore

KAFKA_BROKER = 'localhost:9092'
METER_TOPIC = 'meter-readings'
RESULT_TOPIC = 'analysis-results'

consumer_conf = {
    'bootstrap.servers': KAFKA_BROKER,
    'group.id': 'python-analytics-group',
    'auto.offset.reset': 'earliest'
}
producer_conf = {'bootstrap.servers': KAFKA_BROKER}

consumer = Consumer(consumer_conf)
producer = Producer(producer_conf)

consumer.subscribe([METER_TOPIC])

print('Python analytics service started. Waiting for meter readings...')

try:
    while True:
        msg = consumer.poll(1.0)
        if msg is None:
            continue
        if msg.error():
            print(f"Consumer error: {msg.error()}")
            continue

        try:
            # Parse the meter reading (assume JSON)
            reading = json.loads(msg.value().decode('utf-8'))

            # Ensure reading is a dictionary
            if not isinstance(reading, dict):
                print(f"Invalid message format: expected dictionary, got {type(reading)}")
                continue

            # Dummy analysis: calculate a random anomaly score
            reading['anomalyScore'] = float(np.random.rand())

            # Send result to Kafka
            producer.produce(RESULT_TOPIC, json.dumps(reading).encode('utf-8'))
            producer.flush()
            print(f"Processed and sent analysis: {reading}")
        except json.JSONDecodeError as e:
            print(f"Failed to parse JSON message: {e}")
            continue
        except Exception as e:
            print(f"Error processing message: {e}")
            continue
except KeyboardInterrupt:
    pass
finally:
    consumer.close()
