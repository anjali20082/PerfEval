from typing import Collection
import pymongo
import random
import json
import sys
import matplotlib.pyplot as plt
import numpy as np
import csv
import os
import datetime

PING_RANGE = 100

MONGO_HOST = "mongodb+srv://admin17080:test123@cluster0.ssjoc.gcp.mongodb.net/TestingApps?retryWrites=true&w=majority"
MONGO_CLIENT = pymongo.MongoClient(MONGO_HOST, ssl=True, ssl_cert_reqs='CERT_NONE')
MONGO_DB = MONGO_CLIENT["TestingApps"]

STUDENT = 'default'
CONNECTION_TYPE = 'default'
LOCATION = '110020'

START_KEY = "startTime"
END_POS_KEY_NO_FIND_ELEMENT = "startTime"
END_POS_KEY_FIND_ELEMENT = "endTime"

STUDENTS_FOLDER = "./Students/"
CONTROLLED_EXP_FOLDER = "./controlled/"


APPS_FOLDER = "./Apps/"
APPS_CORRELATION_FOLDER = "./Apps_correlation_values"

DAYTIMES = ["morning", "evening"]

CONN_TYPES = ["Wifi", "MobileData"]

APPS = [
	"Youtube",
	"Hotstar",
	"Flipkart",
	"Amazon",
	"LinkedIn",
	"Facebook",
	"Telegram",
	"Whatsapp",
	"Dailyhunt",
	"GoogleNews",
	"GoogleMaps",
	"Paytm",
	"Mobikwik"
]

MAX_STUDENT_DISTANCE = 397

OTHER_DETAILS = {
	"saswatip": { "android_os": 10, "distance": 0, "ram": 4, "wifi_speed": 19, "mobile_data_speed": -1 },
	"harshal20086": { "android_os": 10, "distance": 0, "ram": 8, "wifi_speed": 67, "mobile_data_speed": 25},
	"harshal2008": { "android_os": 1, "distance": 0, "ram": 8, "wifi_speed": 67, "mobile_data_speed": 25 },
	"lokesh20076": { "android_os": 10, "distance": 171, "ram": 4, "wifi_speed": 28, "mobile_data_speed": 15 },
	"pradeep20091": { "android_os": 9, "distance": 20, "ram": 6, "wifi_speed": 10, "mobile_data_speed": 2 },
	"bhunesh18280": { "android_os": 10, "distance": 50, "ram": 6, "wifi_speed": 28, "mobile_data_speed": 5.4 },
	"bhunesh1208": { "android_os": 10, "distance": 50, "ram": 6, "wifi_speed": 28, "mobile_data_speed": 5.4 },
	"anjali20082": { "android_os": 10, "distance": 20, "ram": 4, "wifi_speed": 46, "mobile_data_speed": 12 },
	"kajal20131": { "android_os": 11, "distance": 171, "ram": 6, "wifi_speed": 5.4, "mobile_data_speed": 8 },
	"ravi20066": { "android_os": 10, "distance": 0, "ram": 6, "wifi_speed": 140, "mobile_data_speed": 10 },
	"ravik.rathee": { "android_os": 10, "distance": 0, "ram": 6, "wifi_speed": 140, "mobile_data_speed": 10 },
	"kanu20024": { "android_os": 7, "distance": 74, "ram": 2, "wifi_speed": 2.7, "mobile_data_speed": 9.6 },
	"ritam19043": { "android_os": 10, "distance": 10.5, "ram": 3, "wifi_speed": 35, "mobile_data_speed": 3.1 },
	"kanakl": { "android_os": 10, "distance": 0, "ram": 4, "wifi_speed": 19, "mobile_data_speed": -1 },
	"shivani20062": { "android_os": 8, "distance": 0, "ram": 3, "wifi_speed": 10, "mobile_data_speed": 4 },
	"priyankas": { "android_os": 9, "distance": 0, "ram": -1, "wifi_speed": -1, "mobile_data_speed": -1 },
	"sapnac": { "android_os": 10, "distance": 0, "ram": 6, "wifi_speed": 19, "mobile_data_speed": 19.18 },
	"shubhamch": { "android_os": 11, "distance": 240, "ram": 6, "wifi_speed": -1, "mobile_data_speed": -1 },

	"khushboo20060": { "android_os": 9, "distance": 397,  "ram": 4, "wifi_speed": 5.3, "mobile_data_speed": 7.4 },
	"shubham20105": { "android_os": 7, "distance": 273,  "ram": 4, "wifi_speed": 37, "mobile_data_speed": 0.86 },
	"vandana20106": { "android_os": -1, "distance": -1,  "ram": -1, "wifi_speed": -1, "mobile_data_speed": -1 },
	"anubhav20057": { "android_os": 11, "distance": -1,  "ram": 6, "wifi_speed": -1, "mobile_data_speed": -1 },
	"pallab200063": { "android_os": 11, "distance": 0,  "ram": 6, "wifi_speed": 83, "mobile_data_speed": 5.5 },
	"sriza20124": { "android_os": 9, "distance": 0,  "ram": 4, "wifi_speed": 55, "mobile_data_speed": 12 },
	"rahul20117": { "android_os": 11, "distance": 171,  "ram": 4, "wifi_speed": 100, "mobile_data_speed": -1 },
	"jaswanth20061": { "android_os": -1, "distance": -1,  "ram": -1, "wifi_speed": -1, "mobile_data_speed": -1 },
	"shubhamch": { "android_os": 11, "distance": 240,  "ram": 6, "wifi_speed": 11, "mobile_data_speed": 7 },
	"kajal20131": { "android_os": -1, "distance": -1,  "ram": 6, "wifi_speed": -1, "mobile_data_speed": -1 },
	"yashesh20080": { "android_os": -1, "distance": -1,  "ram": -1, "wifi_speed": -1, "mobile_data_speed": -1 },
	"akanksha20048": { "android_os": 11, "distance": -1,  "ram": 6, "wifi_speed": -1, "mobile_data_speed": -1 },
	"shivam20121": { "android_os": 11, "distance": 17,  "ram": 8, "wifi_speed": 150, "mobile_data_speed": -1 },
	"akanksha20048": { "android_os": 11, "distance": 230,  "ram": 6, "wifi_speed": -1, "mobile_data_speed": -1 },
	"pallab20063": { "android_os": 11, "distance": 0,  "ram": 6, "wifi_speed": 83, "mobile_data_speed": 5.5 },
	"akhil20107": { "android_os": 10, "distance": 224,  "ram": 6, "wifi_speed": 150, "mobile_data_speed": 20 },

	"bansal.anisha410": { "android_os": 11, "distance": 155,  "ram": 6, "wifi_speed": 11, "mobile_data_speed": 0.25 },

}

WN_STUDENTS = [
	"shubham20105",
	"saswatip",
	"harshal20086",
	# "harshal2008",
	"lokesh20076",
	"pradeep20091",
	"bhunesh18280",
	# "bhunesh1208",
	"anjali20082",
	"kajal20131",
	"ravi20066",
	# "ravik.rathee",
	"kanu20024",
	"ritam19043",
	"kanakl",
	"shivani20062",
	"priyankas",
	"sapnac",
	"shubhamch",
]

MC_STUDENTS = [
	"khushboo20060",
	"vandana20106",
	"sriza20124",
	"rahul20117",
	"shubhamch",
	"shivam20121",
	"akanksha20048",
	"pallab20063",
	"akhil20107",
	# "shubham20105",
	# "anubhav20057",
	# "bansal.anisha410",
	# "jaswanth20061",
	# "roshan20039",
	# "yashesh20080",
	# "nidhiallwani23",
]


# Constants for controlled experiment analysis
DEVICE_NAME = "controlled"
VERSION_COLOR = {
	1: {
		5: 'b',
		8: 'cyan',
	},
	2: {
		5: 'g',
		8: 'limegreen',
	},
	3: {
		5: 'r',
		8: 'lightcoral',
	},
	4: {
		5: 'y',
		8: 'orange',
	},
	5: {
		5: 'indigo',
		8: 'mediumpurple',
	}
}

APPS_LIST = {
	"youtube": [1, 2, 3],
	# "dailyhunt": [1, 2, 3],
	"googlenews": [1, 2, 3],
	"googlemaps": [1, 2, 3],
	"flipkart": [1, 2, 3],
	"telegram": [1, 2, 3],
	"facebook": [1, 2, 3]
}

BAR_WIDTH = 0.2
ANDROID_OS = 8
GTYPE = None
VERSION_ID = None
VTYPE = None
WHICH_APP = None

STUDENT_DETAILS = {
	"priyankas@iiitd.ac.in" : {
		"android_os": 9, 
		"ram": 4, 
		"distance": 0,
		"pincode": 110059,
	},
	"priyanks@iiitd.ac.in" : {
		"android_os": 9, 
		"ram": 4, 
		"distance": 0,
		"pincode": 110059,
		"copyOf": "priyankas@iiitd.ac.in",
		"experimentLocationIsDelhi" : True
	},
	"akanksha20048@gmail.com" : {
		"android_os": 11, 
		"ram": 6, 
		"distance": 230,
		"pincode": 249180,
		"copyOf": "akanksha20048@iiitd.ac.in"
	},
	"akanksha20048@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 6, 
		"distance": 230,
		"pincode": 249180,
		"usedHotspotAsWifi": True,
	},
	"akhil20107@iiitd.ac.in" : {
		"android_os": 6, 
		"ram": 10, 
		"distance": 224,
		"pincode": 180005,
	},
	"bansal.anisha410@gmail.com" : {
		"android_os": 11, 
		"ram": 6, 
		"distance": 155,
		"pincode": 125001,
	},
	"bhunesh18280@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 6, 
		"distance": 50,
		"pincode": 123401,
	},
	"harshal20086@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 8, 
		"distance": 0,
		"pincode": 122003,
	},
	"kajal20131@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 6, 
		"distance": 230,
		"pincode": 249180,
		"usedHotspotAsWifi": True,
	},
	"kanakl@iiit.ac.in" : {
		"android_os": 10, 
		"ram": 4, 
		"distance": 0,
		"pincode": 110020,
		"copyOf": "kanakl@iiitd.ac.in",
	},
	"kanakl@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 4, 
		"distance": 0,
		"pincode": 110020,
		"experimentLocationIsDelhi" : True
	},
	"khushboo20060@iiitd.ac.in" : {
		"android_os": 9, 
		"ram": 4, 
		"distance": 397,
		"pincode": 484110,
		"usedHotspotAsWifi": True,
	},
	"lokesh20076@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 4, 
		"distance": 171,
		"pincode": 462023,
	},
	"pallab200063@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 6, 
		"distance": 0,
		"pincode": 700097,
		"copyOf": "pallab20063@iiitd.ac.in"
	},
	"pallab20063@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 6, 
		"distance": 0,
		"pincode": 700097,
	},
	"pradeep20091@iiitd.ac.in" : {
		"android_os": 9, 
		"ram": 6, 
		"distance": 20,
		"pincode": 124103,
	},
	"abhivandan17007@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 4, 
		"distance": 12,
		"pincode": 121003,
	},
	"abhi@btp.com" : {
		"android_os": 11, 
		"ram": 4, 
		"distance": 12,
		"pincode": 121003,
		"copyOf": "abhivandan17007@iiitd.ac.in"
	},
	"prince17080@btp.com" : {
		"android_os": 11, 
		"ram": 4, 
		"distance": 0,
		"pincode": 110085,
		"experimentLocationIsDelhi" : True
	},
	"rahul20065@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 4, 
		"distance": 0,
		"pincode": 110001,
		"experimentLocationIsDelhi" : True
	},
	"rahul20117@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 4, 
		"distance": 171,
		"pincode": 462038,
	},
	"ravi20066@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 6, 
		"distance": 0,
		"pincode": 110021,
		"experimentLocationIsDelhi" : True
	},
	"rishabh20038@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 8, 
		"distance": 10,
		"pincode": 201011,
	},
	"rishabh20038@iitd.ac.in" : {
		"android_os": 11, 
		"ram": 8, 
		"distance": 10,
		"pincode": 201011,
		"copyOf": "rishabh20038@iiitd.ac.in"
	},
	"ritam19043@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 8, 
		"distance": 10,
		"pincode": 201011,
	},
	"sapnac@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 6, 
		"distance": 0,
		"pincode": 110020,
		"experimentLocationIsDelhi" : True
	},
	"saswatip@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 4, 
		"distance": 0,
		"pincode": 110020,
		"experimentLocationIsDelhi" : True
	},
	"shivam20121@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 8, 
		"distance": 17,
		"pincode": 201014,
	},
	"shivani20062@iiitd.ac.in" : {
		"android_os": 8, 
		"ram": 3, 
		"distance": 0,
		"pincode": 110020,
		"experimentLocationIsDelhi" : True
	},
	"shubham20105@iiitd.ac.in" : {
		"android_os": 7, 
		"ram": 4, 
		"distance": 273,
		"pincode": 361001,
	},
	"shubhamch@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 6, 
		"distance": 240,
		"pincode": 273003,
		"usedHotspotAsWifi": True,
	},
	"sriza20124@iiitd.ac.in" : {
		"android_os": 9, 
		"ram": 4, 
		"distance": 0,
		"pincode": 700094,
	},
	"vandana20106@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 8, 
		"distance": 0,
		"pincode": 110018,
		"experimentLocationIsDelhi" : True
	},
	"vandana20106iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 8, 
		"distance": 0,
		"pincode": 110018,
		"copyOf": "vandana20106@iiitd.ac.in"
	},
	"vandana20106iiitd@.ac.in" : {
		"android_os": 11, 
		"ram": 8, 
		"distance": 0,
		"pincode": 110018,
		"copyOf": "vandana20106@iiitd.ac.in"
	},
	"taral19392@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 6, 
		"distance": 0,
		"pincode": 110052,
		"experimentLocationIsDelhi" : True
	},
	"karan17058@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 6, 
		"distance": 0,
		"pincode": 110088,
		"experimentLocationIsDelhi" : True
	},
	"sapnac1@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 6, 
		"distance": 0,
		"pincode": 110020,
		"experimentLocationIsDelhi" : True
	},
	"anuj17026@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 4, 
		"distance": 345,
		"pincode": 277001,
	},
	"anjali20082@iiitd.ac.in" : {
		"android_os": 9, 
		"ram": 6, 
		"distance": 35,
		"pincode": 124507,
	},
	"juskirat2000@gmail.com" : {
		"android_os": 10, 
		"ram": 4, 
		"distance": 163,
		"pincode": 135001,
	},
	"shradha20069@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 8, 
		"distance": 110,
		"pincode": 134003,
	},
	"paritosh18063@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 8, 
		"distance": 10,
		"pincode": 201010,
	},
	"apurv17028@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 4, 
		"distance": 0,
		"pincode": 110070,
		"experimentLocationIsDelhi" : True
	},
	"kartikey17242@iiitd.ac.in" : {
		"android_os": 8, 
		"ram": 4, 
		"distance": 0,
		"pincode": 110020,
		"experimentLocationIsDelhi" : True
	},
	"navneet18348@" : {
		"android_os": 10, 
		"ram": 6, 
		"distance": 200,
		"pincode": 243122,
		"copyOf": "navneet18348@iiitd.ac.in"
	},
	"navneet18348@iiitd.ac.in" : {
		"android_os": 10, 
		"ram": 6, 
		"distance": 200,
		"pincode": 243122,
	},
	"rohit18087@iiitd.ac.in" : {
		"android_os": 11, 
		"ram": 8, 
		"distance": 0,
		"pincode": 141010,
	},
	"richikchanda1999@gmail.com" : {
		"android_os": 7, 
		"ram": 4, 
		"distance": 0,
		"pincode": 700050,
	},
	"arani@iiitd.ac.in": {
		"android_os": -1, 
		"ram": -1, 
		"distance": 0,
		"pincode": 110020,
	},
	# ----------------new records------------------------
	"bhagya.sabs27@gmail.com": {
		"android_os": 8,
		"ram": 4,
		"distance": 110,
		"pincode": 134003,
	},
	"geeta.sabs20@gmail.com": {
		"android_os": 11,
		"ram": 4,
		"distance": 110,
		"pincode": 134003,
	},
	"infinix_pro": {
		"android_os": 11,
		"ram": 16,
		"distance": 0,
		"pincode": 110020,
	}
}
datatest = []
for x in STUDENT_DETAILS:
	print(x)
	datatest.append(x)
print(len(datatest))
