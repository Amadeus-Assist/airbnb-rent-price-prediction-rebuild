import mysql.connector

from common.utils import Properties, Config
from modelandpredict.trainhousing import trainhousing

credentials = Properties('../resources/credentials.properties').get_properties()
user = credentials['user']
pw = credentials['pw']
address = credentials['address']
db = credentials['db']
db_conn = mysql.connector.connect(
    host=address,
    user=user,
    password=pw,
    database=db,
)
config = Config.get_instance()
valid_cities = config['housing_valid_cities'].split('/')
for city in valid_cities:
    trainhousing(city, db_conn)
db_conn.close()
