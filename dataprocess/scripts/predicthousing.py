import mysql.connector
import sqlalchemy

from common.utils import Properties, Config
from modelandpredict.trainhousing import predicthousing

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
engine = sqlalchemy.create_engine("mysql+pymysql://{user}:{pw}@{address}/{db}"
                                  .format(user=user,
                                          pw=pw,
                                          address=address,
                                          db=db))
config = Config.get_instance()
valid_cities = config['housing_valid_cities'].split('/')
sql = "DELETE FROM housing_median_prediction"
cursor = db_conn.cursor()
cursor.execute(sql)
db_conn.commit()
for city in valid_cities:
    predicthousing(city, db_conn, engine)
db_conn.close()
