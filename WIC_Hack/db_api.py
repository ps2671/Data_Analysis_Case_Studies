from flask import Flask
from flask.ext.restplus import Api
from flask.ext.restplus import fields
from sklearn.externals import joblib

app = Flask(__name__)

api = Api(
   app, 
   version='1.0', 
   title='Find your Dog',
   description='A simple Dog Prediction API')

ns = api.namespace('predict_dog', 
   description='Predict your Dog Breed Suited to your needs')

parser = api.parser()
parser.add_argument(
   'Gender',
   type=float, 
   required=True, 
   help='Please input your gender. Put 1 for Female and 2 for Male.',
   location='form')
parser.add_argument(
   'Age',
   type=float, 
   required=True, 
   help='Please input your age.',
   location='form')
parser.add_argument(
   'Income',
   type=float, 
   required=True, 
   help='Please input your annual income.',
   location='form')
parser.add_argument(
   'MaritalStatus',
   type=float, 
   required=True, 
   help='Please put 1 for married, and 2 for not married.',
   location='form')
parser.add_argument(
   'Kids',
   type=float, 
   required=True, 
   help='Please put 1 if you have kids, and 2 if you do not have kids.',
   location='form')
parser.add_argument(
   'Proximity',
   type=float, 
   required=True, 
   help='Please input the distance in miles of your house from the closest dog shelter.',
   location='form')


resource_fields = api.model('Resource', {
    'result': fields.String,
})

from flask.ext.restplus import Resource
@ns.route('/')
class CreditApi(Resource):

   @api.doc(parser=parser)
   @api.marshal_with(resource_fields)
   def post(self):
     args = parser.parse_args()
     result = self.get_result(args)

     return result, 201

   def get_result(self, args):
      gender = args["Gender"]
      age = args["Age"]
      income = args["Income"]
      maritalStatus = args["MaritalStatus"]
      kids = args["Kids"]
      proximity = args["Proximity"]


      from pandas import DataFrame
      df = DataFrame([[
         age,
         gender,
         income,
         maritalStatus,
         kids,
         proximity,
      ]])
		#Change file path to point to location of nb.pkl file.
      clf = joblib.load('C:/nb.pkl');

      result = clf.predict(df)
      print(result)
      if(result[0] == 1): 
         result = "labrador" 
      elif(result[0] == 2):
         result = "pitbull"
      elif(result[0] == 3):
         result = "chihuahua" 
      else: 
         result = "None"

      return {
         "result": result
      }

if __name__ == '__main__':
    app.run(debug=True)

