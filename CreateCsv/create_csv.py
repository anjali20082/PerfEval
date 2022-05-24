from datetime import time
from re import S

from matplotlib.pyplot import contour, new_figure_manager
from const import *

DAYTIME = "morning"

def datetimeToMS(dt):
	epoch = datetime.datetime(1970,1,1)
	td = dt - epoch
	return str(datetime.datetime.timestamp(dt)).split('.')[0]


def checkTimestamp(x):
	global DAYTIME
	if (x[2].isnumeric()):
		y = x[:4]
		m = x[5:7]
		d = x[8:10]
		x = x.split(' ')[1]
		h = int(x[:x.find(":")])
		if (sys.argv[1] == "controlled"):
			DAYTIME = "morning"
		if h >= 8 and h < 17:
			DAYTIME = "morning"
		else:
			DAYTIME = "evening"

		x = d + '-' + m + '-' + y + ' ' + x
	
	# print("time: ", x)
	dt = datetime.datetime.strptime(x, "%d-%m-%Y %H:%M:%S")
	return datetimeToMS(dt)


def time_between_commands(commands, startPos, endPos, eKey=END_POS_KEY_FIND_ELEMENT, sKey=START_KEY):
	if (endPos >= startPos):
		return commands[endPos][eKey] - commands[startPos][sKey]
	
	return 0


def time_from_commands(commands, clickPos=None, pressKeyPos=None, isElementDisplayed=True, app=None, sendKeysPos=None):
	clickCounter = 0
	pressKeyCounter = 0
	sendKeysCounter = 0
	n = len(commands)

	startPos = None
	endPos = None
	
	includeTime = 0
	excludeTime = 0

	for i in range(n):
		command_time = time_between_commands(commands, startPos=i, endPos=i)
		if (commands[i]['cmd'] == 'click'):
			if (clickPos != None):
				clickCounter += 1

				if (clickCounter == clickPos):
					startPos = i
					includeTime += command_time
		
		elif (commands[i]['cmd'] == 'pressKeyCode'):
			if (pressKeyPos != None):
				pressKeyCounter += 1

				if (pressKeyCounter == pressKeyPos):
					startPos = i
					includeTime += command_time

		elif (commands[i]['cmd'] == 'setValue'):
			if (app == "Hotstar"):
				pass
				# print("BBBBBBBBBBBBBBBB", sendKeysPos)
				
			if (sendKeysPos != None):
				sendKeysCounter += 1

				if (sendKeysCounter == sendKeysPos):
					startPos = i
					includeTime += command_time

		elif (commands[i]['cmd'] == 'elementDisplayed'):
			if (startPos != None and isElementDisplayed):
				endPos = i
				includeTime += command_time
				break
		
		elif (commands[i]['cmd'] == 'findElement'):
			if (startPos != None):
				if (app == None):
					excludeTime += command_time
				else:
					includeTime += command_time
					break
		
		elif (commands[i]['cmd'] == 'findElements'):
			if (startPos != None):
				if (app == None):
					excludeTime += command_time
				else:
					includeTime += command_time

	timeTaken = includeTime
	# exit(0)
	return timeTaken


def getFeatureTotalTime(times):
	total_time = 0
	for key in times.keys():
		total_time += times[key]
	
	return total_time


def get_controlled_json_time(version, app, feature, json_commands, reason):
	global ANDROID_OS
	commands = json.loads(json_commands)['commands']
	times = {}
	tx = {}
	rx = {}

	if (app == "Youtube" and feature == "play video"):
		times["search video page"] = time_from_commands(commands, pressKeyPos=1)
		times["play video page"] = time_from_commands(commands, clickPos=2)
		# times[feature] = getFeatureTotalTime(times)

	# elif (app == "Youtube" and feature == "SearchVid"):
	# 	times["search video page"] = time_from_commands(commands, pressKeyPos=1)
	# 	# times["play video page"] = time_from_commands(commands, clickPos=2)
	# 	# times[feature] = getFeatureTotalTime(times)
	
	elif  (app == "Youtube" and feature == "open channel page"):
		times["search channel page"] = time_from_commands(commands, pressKeyPos=1)
		# times["open channel page"] = time_from_commands(commands, clickPos=2)
		# times["show channel page"] = times["search channel page"] + times["open channel page"]

	

	elif  (app == "Hotstar" and feature == "open trending page"):
		times[feature] = time_from_commands(commands, clickPos=2)


	elif  (app == "Hotstar" and feature == "play video"): 
		# print(app, feature)
		# print(json.dumps(commands, indent=2))

		times["search video page"] = time_from_commands(commands, app="Hotstar", sendKeysPos=1)
		times["play video page"] = time_from_commands(commands, clickPos=2)

	# elif  (app == "Hotstar" and feature == "SearchVid"): 
	# 	# print(app, feature)
	# 	# print(json.dumps(commands, indent=2))

	# 	times["search video page"] = time_from_commands(commands, app="Hotstar", sendKeysPos=1)
	# 	# times["play video page"] = time_from_commands(commands, clickPos=2)

	elif  (app == "LinkedIn" and feature == "view profile"):
		times["my profile page"] = time_from_commands(commands, clickPos=2)

	elif  (app == "LinkedIn" and feature == "search personality"):
		times["search results page"] = time_from_commands(commands, pressKeyPos=1)
		# times["person profile page"] = time_from_commands(commands, clickPos=2)
	
	elif  (app == "LinkedIn" and feature == "check my connections"):
		times["my connections page"] = time_from_commands(commands, clickPos=3)

	elif  (app == "Amazon" and feature == "search product"):
		times["search results page"] = time_from_commands(commands, pressKeyPos=1)
		times["product profile page"] = time_from_commands(commands, clickPos=2)
		times["add to cart clicked"] = time_from_commands(commands, clickPos=3)
		times["go to cart clicked"] = time_from_commands(commands, clickPos=4)
		times["remove from cart clicked"] = time_from_commands(commands, clickPos=5)

	elif  (app == "GoogleMaps" and feature == "search place"):
		times[feature] = time_from_commands(commands, pressKeyPos=1, isElementDisplayed=False, app="GoogleMaps")
	
	elif  (app == "GoogleNews" and feature == "search news"):
		# print(app, feature, version)
		# print(json.dumps(commands, indent=2))
		times[feature] = time_from_commands(commands, clickPos=2)

	elif  (app == "Telegram" and feature == "send message"):
		if (int(ANDROID_OS) == 8):
			times[feature] = time_from_commands(commands, clickPos=4)
		else:
			times[feature] = time_from_commands(commands, clickPos=5)
	
	elif  (app == "Whatsapp" and feature == "send message"):
		times["sending message"] = time_from_commands(commands, clickPos=2, app="Whatsapp")

	elif  (app == "Dailyhunt" and feature == "search news"):
		# times["search results page"] = time_from_commands(commands, pressKeyPos=1, isElementDisplayed=False, app="Dailyhunt")
		times["opening news page"] = time_from_commands(commands, clickPos=2)


	# elif (app == "Flipkart" and feature == "Search"):
	# 	if (int(ANDROID_OS) == 8):
	# 		times["search product result"] = time_from_commands(commands, clickPos=2)
	# 		# times["product profile"] = time_from_commands(commands, clickPos=7)
	# 		# times["add to cart"] = time_from_commands(commands, clickPos=8)
	# 		# times["go to cart"] = time_from_commands(commands, clickPos=9)
	# 		# times["remove from cart"] = time_from_commands(commands, clickPos=10)

	# 		# times[feature] = getFeatureTotalTime(times)

	# elif (app == "Flipkart" and feature == "Profile"):
	# 	if (int(ANDROID_OS) == 8):
	# 		times["search product result"] = time_from_commands(commands, clickPos=2)
	# 		times["product profile"] = time_from_commands(commands, clickPos=3)
	# 		# times["add to cart"] = time_from_commands(commands, clickPos=8)
	# 		# times["go to cart"] = time_from_commands(commands, clickPos=9)
	# 		# times["remove from cart"] = time_from_commands(commands, clickPos=10)

	# 		# times[feature] = getFeatureTotalTime(times)

	# elif (app == "Flipkart" and feature == "Add"):
	# 	if (int(ANDROID_OS) == 8):
	# 		times["search product result"] = time_from_commands(commands, clickPos=2)
	# 		times["product profile"] = time_from_commands(commands, clickPos=3)
	# 		times["add to cart"] = time_from_commands(commands, clickPos=4)
	# 		# times["go to cart"] = time_from_commands(commands, clickPos=9)
	# 		# times["remove from cart"] = time_from_commands(commands, clickPos=10)

	# 		# times[feature] = getFeatureTotalTime(times)

	# elif (app == "Flipkart" and feature == "GoCart"):
	# 	if (int(ANDROID_OS) == 8):
	# 		times["search product result"] = time_from_commands(commands, clickPos=2)
	# 		times["product profile"] = time_from_commands(commands, clickPos=3)
	# 		times["add to cart"] = time_from_commands(commands, clickPos=4)
	# 		times["go to cart"] = time_from_commands(commands, clickPos=4)
	# 		# times["remove from cart"] = time_from_commands(commands, clickPos=10)

	# 		# times[feature] = getFeatureTotalTime(times)

	elif (app == "Flipkart" and feature == "search product"):
		if (int(ANDROID_OS) == 8):
			times["search product result"] = time_from_commands(commands, clickPos=2)
			times["product profile"] = time_from_commands(commands, clickPos=3)
			times["add to cart"] = time_from_commands(commands, clickPos=4)
			times["go to cart"] = time_from_commands(commands, clickPos=4)
			times["remove from cart"] = time_from_commands(commands, clickPos=6)

			# times[feature] = getFeatureTotalTime(times)

		
	

	# elif  (app == "Facebook" and (feature == "search personality") or len(commands) < 15):
	# 		times["search results page"] = time_from_commands(commands, pressKeyPos=1, isElementDisplayed=False, app="Facebook")
	# 		# times["person profile page"] = time_from_commands(commands, clickPos=3, isElementDisplayed=False, app="Facebook")

	elif  (app == "Facebook" and (feature == "view profile") or len(commands) < 15):
			times["search results page"] = time_from_commands(commands, pressKeyPos=1, isElementDisplayed=False, app="Facebook")
			times["person profile page"] = time_from_commands(commands, clickPos=4, isElementDisplayed=False, app="Facebook")


	elif  (app == "Facebook"):
		# print(app, feature, version)
		# print(json.dumps(commands, indent=2))
		if int(version[-1]) < 2:
			times["post in a group"] = time_from_commands(commands, clickPos=8)
		else:
			times["post in a group"] = time_from_commands(commands, clickPos=6)


	return times


def get_time_from_json(app, feature, json_commands, reason):
	global STUDENT, CONNECTION_TYPE, LOCATION
	
	commands = json.loads(json_commands)['commands']

	times = {}
	if (app == "Youtube" and feature == "play video"):
		times["search video page"] = time_from_commands(commands, pressKeyPos=1)
		times["play video page"] = time_from_commands(commands, clickPos=2)
	
	elif  (app == "Youtube" and feature == "open channel page"):
		times["search channel page"] = time_from_commands(commands, pressKeyPos=1)
		times["open channel page"] = time_from_commands(commands, clickPos=2)

	elif  (app == "Hotstar" and feature == "open trending page"):
		times[feature] = time_from_commands(commands, clickPos=2)

	elif  (app == "Hotstar" and feature == "search video"):
		# print(app, feature)
		# print(json.dumps(commands, indent=2))

		times["search video page"] = time_from_commands(commands, app="Hotstar", sendKeysPos=1)
		times["play video page"] = time_from_commands(commands, clickPos=2)

	elif  (app == "LinkedIn" and feature == "view profile"):
		times["my profile page"] = time_from_commands(commands, clickPos=2)

	elif  (app == "LinkedIn" and feature == "search personality"):
		times["search results page"] = time_from_commands(commands, pressKeyPos=1)
		times["person profile page"] = time_from_commands(commands, clickPos=2)
	
	elif  (app == "LinkedIn" and feature == "check my connections"):
		times["my connections page"] = time_from_commands(commands, clickPos=3)

	elif  (app == "Flipkart" and feature == "search product"):
		times["search results page"] = time_from_commands(commands, clickPos=2)
		times["product profile page"] = time_from_commands(commands, clickPos=3)
		if (reason == "add to cart clicked"):
			times["add to cart clicked"] = time_from_commands(commands, clickPos=4)
			times["go to cart clicked"] = time_from_commands(commands, clickPos=6)
			times["remove from cart clicked"] = time_from_commands(commands, clickPos=8)
		else:
			times["go to cart clicked"] = time_from_commands(commands, clickPos=4)
			times["remove from cart clicked"] = time_from_commands(commands, clickPos=6)
		

	elif  (app == "Amazon" and feature == "search product"):
		times["search results page"] = time_from_commands(commands, pressKeyPos=1)
		times["product profile page"] = time_from_commands(commands, clickPos=2)
		times["add to cart clicked"] = time_from_commands(commands, clickPos=3)
		times["go to cart clicked"] = time_from_commands(commands, clickPos=4)
		times["remove from cart clicked"] = time_from_commands(commands, clickPos=5)

	elif  (app == "Telegram" and feature == "send message"):
		times["sending message"] = time_from_commands(commands, clickPos=3)

	elif  (app == "Whatsapp" and feature == "send message"):
		times["sending message"] = time_from_commands(commands, clickPos=2, app="Whatsapp")

	elif  (app == "GoogleMaps" and feature == "search place"):
		times["searching place"] = time_from_commands(commands, pressKeyPos=1, isElementDisplayed=False, app="GoogleMaps")

	elif  (app == "Dailyhunt" and feature == "search news"):
		# times["search results page"] = time_from_commands(commands, pressKeyPos=1, isElementDisplayed=False, app="Dailyhunt")
		times["opening news page"] = time_from_commands(commands, clickPos=2)

	elif  (app == "GoogleNews" and feature == "search news"):
		times["opening news page"] = time_from_commands(commands, clickPos=2, isElementDisplayed=False, app="GoogleNews")

	elif  (app == "Facebook" and (feature == "search personality") or len(commands) < 15):
		times["search results page"] = time_from_commands(commands, pressKeyPos=1, isElementDisplayed=False, app="Facebook")
		times["person profile page"] = time_from_commands(commands, clickPos=3, isElementDisplayed=False, app="Facebook")

	elif  (app == "Facebook" and feature == "post in a group"):
		times[feature] = time_from_commands(commands, clickPos=6, isElementDisplayed=False, app="Facebook")

	# elif  (app == "Mobikwik" and feature == "transaction from wallet"):
	# 	times[feature] = time_from_commands(commands, clickPos=4)

	elif  (app == "Paytm" and feature == "transaction from wallet"):
		if (len(commands) < 42):
			times[feature] = time_from_commands(commands, clickPos=5)
		else:
			times[feature] = time_from_commands(commands, clickPos=9)
		
	new_times = {}
	for key in times.keys():
		if (times[key] != 0):
			new_times[key] = times[key]

	return new_times


def get_data_consumed_single_app(app, before_info, after_info, doc):
	
	if before_info is None or after_info is None:
		return 0, 0, 0, 0
	
	data_before = before_info.splitlines()
	data_after = after_info.splitlines()
	
	length = len(data_before)
	
	if(len(data_after) != len(data_before)):
		return 0, 0, 0, 0

	if app == "GoogleNews":
		app = "google news"

	elif app == "GoogleMaps":
		app = "maps"
	
	app = app.lower()

	rx, tx, n_tcp, n_udp = "---", "---", "---", "---" 

	for ind in range(length):
		before = data_before[ind].split(":")
		after = data_after[ind].split(":")
		
		if before[0].lower() == app:
			rx = int(after[1]) - int(before[1])
			tx = int(after[2]) - int(before[2])
			n_tcp = int(after[3])
			n_udp = int(after[4])

			# if rx == 0:
			# 	rx = "---"
			
			# if tx == 0:
			# 	tx =  "---"
			
			# if n_tcp == 0:
			# 	n_tcp =  "---"
			
			# if n_udp == 0:
			# 	n_udp =  "---"

			break

	return rx, tx, n_tcp, n_udp


def assign_test_name(app, feature):
	if app == "Amazon" or app == "Flipkart":
		feature = "search product"
	# elif app =="Flipkart" and feature == "Search":
	# 	feature = feature
	# elif app =="Flipkart" and feature == "Profile":
	# 	feature = feature
	# elif app =="Flipkart" and feature == "Add":
	# 	feature = feature
	# elif app =="Flipkart" and feature == "GoCart":
	# 	feature = feature
	elif app =="Flipkart" and feature == "Remove":
		feature = "search product"	

	elif app == "Whatsapp" or app == "Telegram":
		feature = "send message"
	elif app == "Mobikwik" or app == "Paytm":
		feature = "transaction from wallet"
	elif app == "Dailyhunt" or app == "GoogleNews":
		feature = "search news"
	elif app == "GoogleMaps":
		feature = "search place"

	elif feature == "searchChannel" or feature == "find channel":
		feature = "open channel page"
	elif feature == "playVideo" or feature == "play test" or feature == "PlayVid":
		feature = "play video"
	elif app == "Hotstar" and (feature == "search" or feature == "Search Test"):
		feature = "search video"
	elif app == "Hotstar" and (feature == "trending" or feature == "Trending Test"):
		feature = "open trending page"
	elif feature == "search" or feature == "search person":
		feature = "search personality"
	elif feature == "profile" or feature == "PersonProfile":
		feature = "view profile"
	elif feature == "myConnections":
		feature = "check my connections"
	elif feature == "post":
		feature = "post in a group"	
	else:
		feature = feature
	return feature


def validCollection(collection):
	if (sys.argv[1] == "controlled"):
		return True

	if (collection in STUDENT_DETAILS.keys()):
		return True
	
	return False

	# if "@" not in collection:
	# 	return False
	
	# if ('prince' in collection or 'rishabh20038' in collection):
	# 	return False
	
	# return True


def getCollectionNames():
	global GTYPE, WHICH_APP, ANDROID_OS, VTYPE, VERSION_ID
	collectionNames = []
	if (VTYPE == "compare_versions"):
		for versionId in VERSION_ID:
			collectionName = DEVICE_NAME + "-os" + str(ANDROID_OS) + "_v" + str(versionId)
			collectionNames += [collectionName]
	else:
		collectionName = DEVICE_NAME + "-os" + str(ANDROID_OS) + "_v" + str(VERSION_ID)
		collectionNames += [collectionName]

	return collectionNames


def getCmdParameters():
	global GTYPE, WHICH_APP, ANDROID_OS, VTYPE, VERSION_ID
	GTYPE = sys.argv[2]

	if GTYPE == "compare_os":
		ANDROID_OS = list(map(int, sys.argv[3].split()))
	elif GTYPE == "os":
		ANDROID_OS = int(sys.argv[3])
	
	VTYPE = sys.argv[4]
	if (VTYPE == "compare_versions"):
		VERSION_ID = list(map(int, sys.argv[5].split()))
	elif (VTYPE == "version"):
		VERSION_ID = int(sys.argv[5])
	
	WHICH_APP = sys.argv[6].lower()

	return getCollectionNames()


def getDataFromMongoDB(collectionName: str):
	# print("Getting Data for ", collectionName)
	MONGO_CLIENT = pymongo.MongoClient(MONGO_HOST)
	MONGO_DB = MONGO_CLIENT["TestingApps"]
	MONGO_COLLECTION = MONGO_DB[collectionName]

	data = {}
	for doc in MONGO_COLLECTION.find():
		if (u'status' in doc.keys() and doc[u'status']):
			app = doc[u'app'].lower();
			timesObj = dict(doc[u'times'])
			if (app not in data.keys()):
				data[app] = {};
				
				for key in timesObj.keys():
					data[app][key] = {}
					data[app][key]['count'] = 0
					data[app][key]['times'] = []
					data[app][key]['min'] = -1
					data[app][key]['max'] = -1
					data[app][key]['avg'] = 0
			else:
				for key in timesObj.keys():
					if (key not in data[app].keys()):
						data[app][key] = {}
						data[app][key]['min'] = -1
						data[app][key]['max'] = -1
						data[app][key]['avg'] = 0
						data[app][key]['count'] = 0
						data[app][key]['times'] = []

			for key in timesObj.keys():
				data[app][key]['count'] += 1
				data[app][key]['times'] += [timesObj[key]]

				if (data[app][key]['min'] == -1 or data[app][key]['min'] > timesObj[key]):
					data[app][key]['min'] = timesObj[key]

				if (data[app][key]['max'] == -1 or data[app][key]['max'] < timesObj[key]):
					data[app][key]['max'] = timesObj[key]

				avg = data[app][key]['avg']
				count = data[app][key]['count']
				data[app][key]['avg'] = (avg * (count-1) + timesObj[key]) / count

		# print(WHICH_APP)
	if (VTYPE == "version"):
		filename = "json/" + collectionName + ".json"
		# print(filename)
		with open(filename, 'w') as jf:
			json.dump(data, jf, indent=4)
	
	return data		


# def draw_graphs_of_version_comparison(DATA):
# 	plt.rcParams.update({'figure.autolayout': True})
# 	plt.rcParams['figure.figsize'] = 16, 9
# 	SMALL_SIZE = 20
# 	# SMALL_SIZE = 10
# 	MEDIUM_SIZE = 27
# 	# MEDIUM_SIZE = 12
# 	BIGGER_SIZE = 40
# 	plt.rc('font', size=MEDIUM_SIZE)          # controls default text sizes
# 	plt.rc('axes', titlesize=BIGGER_SIZE)     # fontsize of the axes title
# 	plt.rc('xtick', labelsize=MEDIUM_SIZE)    # fontsize of the tick labels
# 	plt.rc('ytick', labelsize=MEDIUM_SIZE)    # fontsize of the tick labels
# 	plt.rc('legend', fontsize=SMALL_SIZE)    # legend fontsize
# 	plt.rc('figure', titlesize=BIGGER_SIZE)  # fontsize of the figure title
# 	# plt.rcParams.update({'font.size': 12})
	
# 	boxprops = dict(linewidth=2)
# 	medianprops = dict(linewidth=2)
# 	flierprops = dict(marker='o', markerfacecolor='green', markersize=12,markeredgecolor='none')

# 	versionIds = VERSION_ID
# 	# print(versionIds)
	
# 	# print("Creating comparison graphs of app:", WHICH_APP)

# 	apps = []
# 	if WHICH_APP == "all":
# 		apps = list(APPS_LIST.keys())
# 	else:
# 		apps = WHICH_APP.split(' ')

# 	for app in apps:
# 		for test in DATA[app].keys():
# 			# fig, ax = plt.subplots(nrows = 3, ncols= 2, sharey = False, figsize = (13,13))

# 			features_to_save = sys.argv[7]
# 			x = features_to_save.split(' ')

# 			features_to_save = "os" + str(ANDROID_OS) + "/" + features_to_save

# 			if (not os.path.isdir(os.path.join(CONTROLLED_EXP_FOLDER, features_to_save))):
# 				os.mkdir(os.path.join(CONTROLLED_EXP_FOLDER, features_to_save))
			
# 			fig = plt.figure()
# 			r = 0
# 			c = 0 
# 			cnt = 1
# 			# print("features to save:", features_to_save)
# 			for feature in DATA[app][test].keys():
# 				# print(r,c)
# 				# print(app, feature )
# 				versionLabels = []
# 				ylabel = ""
				
# 				if (features_to_save != "all" and feature not in features_to_save):
# 					continue

# 				if feature == "rx":
# 					ylabel = "No. Of Rx Bytes (Mb)"

# 				elif feature == "tx":
# 					ylabel = "No. Of Tx Bytes (Mb)"
					
# 				elif feature == "n_tcp":
# 					ylabel = "No. of TCP connections"

# 				elif feature == "n_udp":
# 					ylabel = "No. of UDP connections"

# 				elif feature == "ping_median_google":
# 					ylabel = "ping google.com"
				
# 				elif feature == "ping_median_amazon":
# 					ylabel = "ping amazon.com"
				
# 				elif feature == "ping_median_mobikwik":
# 					ylabel = "ping mobikwik.com"
				
# 				else:
# 					ylabel = "Response Time (ms)"
				
# 				y_axis_data = []
				
# 				title = ""

					
# 				max_version_wise = -1

# 				for versionId in DATA[app][test][feature].keys():
# 					# print(app, ":", versionId, ":", test, ":", feature)
# 					versionLabels += [str(versionId)]	
# 					data = DATA[app][test][feature][versionId]
# 					y_axis_data += [data]
# 					max_version_wise = max(max_version_wise, max(data))
					
				
# 				ax = fig.add_subplot(1, 3, cnt)
# 				cnt += 1
# 				ax.boxplot(y_axis_data, labels = versionLabels, boxprops=boxprops, medianprops=medianprops)
# 				ax.set_title(title)
# 				ax.set_ylabel(ylabel)
# 				if (app == "googlemaps" and feature in ["rx"]):
# 					y_ticks = [i for i in range(1, int(max_version_wise) + 1)]
# 					ax.set_yticks(y_ticks)
				
# 				ax.set_ylim(bottom=0)
				
# 				ax.grid()

# 				if c == 1:
# 					r += 1
# 					c = 0
# 				else:
# 					c += 1

# 			output_path = CONTROLLED_EXP_FOLDER + features_to_save + "/" + app + "_" + test + ".png"
# 				# plt.show()
			
# 			fig.add_subplot(111, frameon=False)
# 			fig.subplots_adjust(hspace=0.4)
# 			plt.tick_params(labelcolor='none', top=False, bottom=False, left=False, right=False)
# 			# plt.xlabel("Versions: " + str(ANDROID_OS) + ":" + app + ":" + test, fontsize = 20)
# 			plt.xlabel("Versions", fontsize = 20)
# 			plt.savefig(output_path)


def getControlledExpData():
	global GTYPE, VTYPE
	collectionName = getCmdParameters()

	if (GTYPE == "os"):
		if (VTYPE == "version"):
			pass
			# draw_graphs_of_specific_os_and_version()
		elif (VTYPE == "compare_versions"):
			draw_graphs_of_version_comparison()
	elif (GTYPE == "compare_os"):
		pass
		# draw_graphs_of_os_comparison()
 

def getPingRange(i, rows):
	t1 = int(rows[i][1])
	# print(t1)
	ping_range = {
		"google": [],
		"amazon": [],
		"mobikwik": []
	}
	# print(i)
	for j in range(i - 1, 0, -1):
		# print(rows[j])
		if ("ping" not in rows[j][0]):
			# print("here")
			continue
		# print("outside")
		t2 = int(rows[j][1])
		if (t1 - t2 > PING_RANGE):
			break

		if ("Google" in rows[j][0]):
			ping_range["google"].append(float(rows[j][2]))
		
		if ("Amazon" in rows[j][0]):
			ping_range["amazon"].append(float(rows[j][2]))

		if ("Mobikwik" in rows[j][0]):
			ping_range["mobikwik"].append(float(rows[j][2]))


	
	for j in range(i+1, len(rows)):
		if ("ping" not in rows[j][0]):
			continue
		
		t2 = int(rows[j][1])
		if (t2 - t1 > PING_RANGE):
			break

		if ("Google" in rows[j][0]):
			ping_range["google"].append(float(rows[j][2]))
		
		if ("Amazon" in rows[j][0]):
			ping_range["amazon"].append(float(rows[j][2]))

		if ("Mobikwik" in rows[j][0]):
			ping_range["mobikwik"].append(float(rows[j][2]))

	return ping_range


def get_bytes(version, app, feature, json_commands, rx_test, tx_test):
	global ANDROID_OS
	commands = json.loads(json_commands)['commands']
	tx = {}
	rx = {}

	if (app == "Youtube" and feature == "play video"):
		rx["search video page"] = rx_test.split(":")[0]
		rx["play video page"] = rx_test.split(":")[1]
		tx["search video page"] = tx_test.split(":")[0]
		tx["play video page"] = tx_test.split(":")[1]


	elif (app == "Youtube" and feature == "open channel page"):
		rx["search channel page"] = rx_test.split(":")[0]
		tx["search channel page"] = tx_test.split(":")[0]

	elif (app == "Hotstar" and feature == "open trending page"):
		rx["search trending"] = rx_test.split(":")[0]
		tx["search trending"] = tx_test.split(":")[0]

	elif (app == "Hotstar" and feature == "play video"):
		rx["search video page"] = rx_test.split(":")[0]
		rx["play video page"] = rx_test.split(":")[1]
		tx["search video page"] = tx_test.split(":")[0]
		tx["play video page"] = tx_test.split(":")[1]

	elif (app == "LinkedIn" and feature == "view profile"):
		rx["my profile page"] = rx_test.split(":")[0]
		tx["my profile page"] = tx_test.split(":")[0]

	elif (app == "LinkedIn" and feature == "search personality"):
		rx["search results page"] = rx_test.split(":")[0]
		tx["search results page"] = tx_test.split(":")[0]

	elif (app == "LinkedIn" and feature == "check my connections"):
		rx["my connections page"] = rx_test.split(":")[0]
		tx["my connections page"] = tx_test.split(":")[0]

	elif (app == "Amazon" and feature == "search product"):
		rx["search results page"] = rx_test.split(":")[0]
		rx["product profile page"] = rx_test.split(":")[1]
		rx["add to cart clicked"] = rx_test.split(":")[2]
		rx["go to cart clicked"] = rx_test.split(":")[3]
		rx["remove from cart clicked"] = rx_test.split(":")[4]

		tx["search results page"] = tx_test.split(":")[0]
		tx["product profile page"] = tx_test.split(":")[1]
		tx["add to cart clicked"] = tx_test.split(":")[2]
		tx["go to cart clicked"] = tx_test.split(":")[3]
		tx["remove from cart clicked"] = tx_test.split(":")[4]

	elif (app == "GoogleMaps" and feature == "search place"):
		rx[feature] = rx_test.split(":")[0]
		tx[feature] = tx_test.split(":")[0]

	elif (app == "GoogleNews" and feature == "search news"):
		rx[feature] = rx_test.split(":")[0]
		tx[feature] = tx_test.split(":")[0]

	elif (app == "Telegram" and feature == "send message"):
		if (int(ANDROID_OS) == 8):
			rx[feature] = rx_test.split(":")[0]
			tx[feature] = tx_test.split(":")[0]
		else:
			rx[feature] = rx_test.split(":")[0]
			tx[feature] = tx_test.split(":")[0]

	elif (app == "Whatsapp" and feature == "send message"):
		rx["sending message"] = rx_test.split(":")[0]
		tx["sending message"] = tx_test.split(":")[0]

	elif (app == "Dailyhunt" and feature == "search news"):
		rx["opening news page"] = rx_test.split(":")[0]
		tx["opening news page"] = tx_test.split(":")[0]

	elif (app == "Flipkart" and feature == "search product"):
		if (int(ANDROID_OS) == 8):
			rx["search product result"] = rx_test.split(":")[0]
			rx["product profile"] = rx_test.split(":")[1]
			rx["add to cart"] = rx_test.split(":")[2]
			rx["go to cart"] = rx_test.split(":")[3]
			rx["remove from cart"] = rx_test.split(":")[4]

			tx["search product result"] = tx_test.split(":")[0]
			tx["product profile"] = tx_test.split(":")[1]
			tx["add to cart"] = tx_test.split(":")[2]
			tx["go to cart"] = tx_test.split(":")[3]
			tx["remove from cart"] = tx_test.split(":")[4]

	elif (app == "Facebook" and (feature == "view profile") or len(commands) < 15):
		rx["search results page"] = rx_test.split(":")[0]
		tx["search results page"] = tx_test.split(":")[0]
		rx["person profile page"] = rx_test.split(":")[1]
		tx["person profile page"] = tx_test.split(":")[1]

	elif (app == "Facebook"):
		# print(app, feature, version)
		# print(json.dumps(commands, indent=2))
		if int(version[-1]) < 2:
			rx["post in a group"] = rx_test.split(":")[0]
			tx["post in a group"] = tx_test.split(":")[0]

		else:
			rx["post in a group"] = rx_test.split(":")[0]
			tx["post in a group"] = tx_test.split(":")[0]

	return rx, tx

	
def write_to_csv():
	global STUDENT, CONNECTION_TYPE, LOCATION
	

	if ("controlled" in sys.argv[1]):
		Collections = getCmdParameters()
		# print("Collections:",Collections)
	elif (len(sys.argv) > 2 and sys.argv[2] == "arani@iiitd.ac.in"):
		Collections = [sys.argv[2]]
	else:
		Collections = MONGO_DB.list_collection_names()
		if ("arani@iiitd.ac.in" in Collections):
			Collections.remove("arani@iiitd.ac.in")	

	fields = [
		"Location",
		"Time",
		"Network Type",
		"App",
		"Feature",
		"Latency",
		"Rx Data Consumed",
		"Tx Data Consumed",
		"Ping Google",
		"Ping Amazon",
		"Ping Mobikwik"
	]

	app_count = {}
	graphs_data = {}
	ping_graphs_data = {}
	students_location = {}
	correlation_matrix = []
	student_conn_types = {}
	controlled_data = {}
	controlled_rows = []

	if (sys.argv[1] in ["student-app-count-mc", "student-app-count-wn", "all"]):
		fields = ["student", "location", "android_version", "ram", "distance_from_nearest_datacenter", "wifi_speed", "mobile_data_speed"]
		for app in APPS:
			for daytime in DAYTIMES:
				for conn_type in CONN_TYPES:
					key = app + "_" + daytime + "_" + conn_type
					fields.append(key)

		output_csv = "student_app_count_" + sys.argv[1][-2:] + ".csv"
		
		with open(output_csv, 'w', newline='') as csvfile:
			csvwriter = csv.writer(csvfile)
			csvwriter.writerow(fields)

	for col in Collections:
		ping_data = {
			"google": {},
			"amazon": {},
			"mobikwik": {},
		}

		if not validCollection(col):
			continue

		col = col.strip()
		if ("controlled" not in sys.argv[1]) and ("copyOf" in STUDENT_DETAILS[col].keys()):
			col = STUDENT_DETAILS[col]["copyOf"]

		STUDENT = col

		
		if len(sys.argv) > 2 and sys.argv[2] == "delhi" and "experimentLocationIsDelhi" not in STUDENT_DETAILS[STUDENT]:
			continue

		elif len(sys.argv) > 2 and sys.argv[2] == "iiitd" and STUDENT_DETAILS[STUDENT]["pincode"] != 110020:
			continue

		# print(STUDENT)

		
		try:
			if (sys.argv[1] == "controlled"):
				pass
				# os.mkdir(os.path.join(CONTROLLED_EXP_FOLDER, STUDENT))
			else:
				os.mkdir(os.path.join(STUDENTS_FOLDER, STUDENT))
		except:
			pass

		MONGO_COLLECTION = MONGO_DB[col]

		rows = []
		ping_rows = []
		latency_rows = []
		student_total_runs = set()
		student_app_feature_count = {}

		i=0

		for doc in MONGO_COLLECTION.find():
			
			# if (u'status' in doc.keys() and doc[u'status']):
			# if(i == 100):
			# 	break
				if u'Location' in doc.keys():
					
					LOCATION = doc[u'Location']
					
					# if (STUDENT == "pradeep20091"):
					# 	print(doc[u'Tests Started at'])

					if (STUDENT not in students_location.keys()):
						students_location[STUDENT] = LOCATION
					
					student_total_runs.add(doc[u'Tests Started at'].split()[0])

				elif u'app' in doc.keys():
					if u'json' in doc.keys():
						
						if len(sys.argv) > 2 and "iiitd" in sys.argv[2] and LOCATION != "110020":
							continue
						
						if (not doc[u'app'] and not doc[u'connType']):
							continue

						app = doc[u'app'].split("_")[0]
						
						if (u'connType' in doc):
							CONNECTION_TYPE = doc[u'connType']
						elif ('connType' in doc):
							CONNECTION_TYPE = doc['connType']

						try:
							if ("Wifi" in CONNECTION_TYPE):
								if ("0" in CONNECTION_TYPE):
									CONNECTION_TYPE = "MobileData"
								else:
									CONNECTION_TYPE = "Wifi"
							else:
								CONNECTION_TYPE = "MobileData"
						except:
							continue

						if (STUDENT not in student_conn_types):
							student_conn_types[STUDENT] = []
						elif (CONNECTION_TYPE not in student_conn_types[STUDENT]):
							student_conn_types[STUDENT].append(CONNECTION_TYPE)
						

						time = doc[u'startedAt'].split('.')[0]
						time = time.replace('/', '-')
						time = checkTimestamp(time)
						

						json_commands = doc[u'json']
						
						if "_" not in doc[u'app']:
							feature = ""
						else:
							feature = doc[u'app'].split("_")[1]

						# print("before  : ", feature)
						feature = assign_test_name(app, feature)
						# print(" after feature: ", feature, " after app : ", app)

						if (sys.argv[1] == "controlled"):
							# tx_test, rx_test = doc[u'tx_bytes'], doc[u'rx_bytes']
							row = [STUDENT[-2:], time, CONNECTION_TYPE, app]
							feature_performance = get_controlled_json_time(STUDENT[-2:], app, feature, json_commands, doc[u'reason'])
							# if (doc[u'status']):
							# 	rx_action, tx_action = get_bytes(STUDENT[-2:], app, feature, json_commands, rx_test, tx_test)

						else:
							row = [LOCATION, time, CONNECTION_TYPE, app]
							feature_performance = get_time_from_json(app, feature, json_commands, doc[u'reason'])
						# print(feature_performance)
						# print("tx-----",tx_action)
						
						if (len(feature_performance) == 0):
							continue

						if (sys.argv[1] == "controlled"):
							key = app + "_" + CONNECTION_TYPE
						else:
							feature += "_" + DAYTIME
							key = app + "_" + DAYTIME + "_" + CONNECTION_TYPE


						if (key not in student_app_feature_count):
							student_app_feature_count[key] = 1
						else:
							student_app_feature_count[key] += 1			

						# for f in feature_performance.keys():
						# 	if (doc[u'status']):
						# 		row.extend([f, feature_performance[f], int(rx_action[f])/(1024*1024), int(tx_action[f])/(1024*1024)])

						# print(row)

						rx, tx, n_tcp, n_udp = get_data_consumed_single_app(app, doc[u'data_before'], doc[u'data_after'], doc)
						# print(rx_action)
						if (sys.argv[1] == "controlled"):
							row.extend(["rx_old", rx, "tx_old", tx, "n_tcp", n_tcp, "n_udp", n_udp])
							controlled_rows.append(row)
							# print(row)
							
						else:
							row.extend(["rx", rx, "tx", tx])
							row.extend([DAYTIME])
							rows.append(row)

						latency_rows.append(row)
						# print(latency_rows)
						# print("tx: -------", tx_action)
						# print("rx: -------", rx_action)
						
				else:
					# print("here")

					if len(sys.argv) > 2 and "iiitd" in sys.argv[2] and LOCATION != "110020":
						continue

					# websites = [u'pingGoogle', u'pingAmazon', u'pingMobikwik']
					websites = [u'pingGoogle']
					
					for pingWebsite in websites:
						if pingWebsite in doc.keys():
							# time_uploaded = doc[u'Time Uploaded']
							json_string = doc[pingWebsite]
							ping_output = json.loads(json_string)
							for ping_row in ping_output:
								if 'rt' in ping_row.keys() and ping_row['rt'] != '' and ping_row['t'] != '':
									rt = [pingWebsite]
									ping_row['rt'] = ping_row['rt'].split(' ')[0]
									if (ping_row['t'][:10].isnumeric()):
										ping_row['t'] = str(ping_row['t'][:10])
									else:
										# Wed 03/31/2021 21:35:37.13 
										ping_row['t'] = ping_row['t'].replace('/', '-')
										ping_row['t'] = ping_row['t'].replace('  ', ' ')
										ping_row['t'] = ping_row['t'].split('.')[0]
										ping = ping_row['t']
										ping = ping.split(' ')
										if (len(ping) == 3):
											(date, time) = ping[1:]
											date = list(map(int, date.split('-')))
											time = list(map(int, time.split(':')))
											dt = datetime.datetime(date[2], date[0], date[1], time[0], time[1], time[2])
											ping_row['t'] = datetimeToMS(dt)
										else:
											if (STUDENT == "karan17058@iiitd.ac.in"):
												date = ''.join(ping[1:6])
												time = ping[6]
											else:
												if (len(ping)) > 2:
													# print(STUDENT, ping)
													exit(0)
												(date, time) = ping[:]

											
											date = date.replace("May-21", "05-2021")
											date = list(map(int, date.split('-')))
											time = list(map(int, time.split(':')))
											if (date[0] <= 31):
												dt = datetime.datetime(date[2], date[1], date[0], time[0], time[1], time[2])
											else:
												dt = datetime.datetime(date[0], date[1], date[2], time[0], time[1], time[2])

											ping_row['t'] = datetimeToMS(dt)
										
									# print(ping_row)
									rt.extend([ping_row['t'], ping_row['rt']])

									if (pingWebsite == u'pingGoogle'):
										website = "google"
									elif (pingWebsite == u'pingAmazon'):
										website = "amazon"
									elif (pingWebsite == u'pingMobikwik'):
										website = "mobikwik"

									if (ping_row['t'] in ping_data[website].keys()):
										ping_data[website][ping_row['t']].append(ping_row['rt'])
									else:
										ping_data[website][ping_row['t']] = [ping_row['rt']]
							
									if (sys.argv[1] == "controlled"):
										controlled_rows.append(rt)
									else:
										rows.append(rt)

									ping_rows.append([LOCATION] + rt)
			# print(controlled_rows)
				i = i+1
		


		if (sys.argv[1] in ["student-app-count-mc", "student-app-count-wn", "all"]):
			flag = False
			CLASS = sys.argv[1][-2:]
			if (CLASS == "mc"):
				if (STUDENT in MC_STUDENTS):
					flag = True
			elif (CLASS == "wn"):
				if (STUDENT in WN_STUDENTS):
					flag = True
				
			if (STUDENT not in STUDENT_DETAILS.keys()):
				flag = False

			if (flag):
				daytimes = ["morning", "evening"]
				data_row = [STUDENT, LOCATION, STUDENT_DETAILS[STUDENT]["android_os"], STUDENT_DETAILS[STUDENT]["ram"], STUDENT_DETAILS[STUDENT]["distance"]]
				for app in APPS:
					for daytime in daytimes:
						for conn_type in CONN_TYPES:
							key = app + "_" + daytime + "_" + conn_type
							if (key in student_app_feature_count):
								data_row.extend([student_app_feature_count[key]])
							else:
								data_row.extend([0])
				
				output_csv = "student_app_count_" + CLASS +".csv"
				with open(output_csv, 'a', newline='') as csvfile:
					csvwriter = csv.writer(csvfile)
					csvwriter.writerow(data_row)

		# rows = sorted(rows, key=lambda x: int(x[1]))
		rows = sorted(controlled_rows, key=lambda x: int(x[1]))
		# print(rows)

		if (sys.argv[1].split("-")[0] in ["pingGoogle_graphs", "find_correlation", "jumbo", "all"]):
			if STUDENT not in STUDENT_DETAILS:
				continue

			count = -1
			for i in range(len(rows)):
				count += 1
				row = rows[i]
				
				if ('ping' in row[0]):
					continue
				
				
				app = row[3]

				ping_range = getPingRange(i, rows)

				
				if (len(ping_range["google"]) == 0 or len(ping_range["amazon"]) == 0 or len(ping_range["mobikwik"]) == 0):
					# print(STUDENT, "NO PING")
					continue	

				ping_median_google = np.median(ping_range["google"])
				ping_median_amazon = np.median(ping_range["amazon"])
				ping_median_mobikwik = np.median(ping_range["mobikwik"])
				
				# location = row[0]
				android_os = STUDENT_DETAILS[STUDENT]["android_os"]
				distance = STUDENT_DETAILS[STUDENT]["distance"]
				ram = STUDENT_DETAILS[STUDENT]["ram"]

				# if (android_os == -1 or distance == -1 or ram == -1 or wifi_speed == -1 or mobile_data_speed == -1):
				# 	continue

				for j in range(4, len(row), 2):
					if (row[j] == "rx"):
						break

					latency = row[j+1]
					net_speed = 0 # wifi
					net_type = 0
					time = 0

					if (sys.argv[1].split("-")[1] == "combined"):
						cor_key = app + "_" + row[-1]
					elif (sys.argv[1].split("-")[1] == "featuretime"):
						cor_key = app + "_" + row[j] + "_" + row[-1]
					elif (sys.argv[1].split("-")[1] == "feature"):
						cor_key = app + "_" + row[j]

					if ("Wifi" in row[2]):
						key = app + "_" + row[j] + "_wifi"
						# net_speed = wifi_speed # wifi
						net_type = 0 # wifi

						if ("usedHotspotAsWifi" in STUDENT_DETAILS[STUDENT].keys()):
							net_type = 2
							if ("copyOf" in STUDENT_DETAILS[STUDENT].keys()  and "usedHotspotAsWifi" in STUDENT_DETAILS[STUDENT_DETAILS[STUDENT]["copyOf"]].keys()):
								net_type = 2

					else:
						key = app + "_" + row[j] + "_md"
						# net_speed = mobile_data_speed # md
						net_type = 1 # wifi

					if (row[-1] == "evening"):
						time = 1

					if (key not in ping_graphs_data):
						ping_graphs_data[key] = []

					correlation_data = [cor_key, STUDENT, android_os, distance / MAX_STUDENT_DISTANCE, ram, latency, time, net_type, ping_median_google, ping_median_amazon, ping_median_mobikwik]
					correlation_matrix += [correlation_data]
					
					ping_graphs_data[key].append([row[j+1], ping_range])


		elif sys.argv[1].split("-")[0] in ["one_csv", "all", "csv"]:
			csv_file = STUDENTS_FOLDER + student + "/all.csv"
			fields = ['date_type', 'date']
			with open(csv_file, 'w', newline='') as csvfile:
				csvwriter = csv.writer(csvfile)
				csvwriter.writerows(rows)


		elif (sys.argv[1].split("-")[0] in ["students_csv", "all", "csv"]):
			for row in rows:
				if ('ping' in row[0]):
					continue

				app = row[3]
				csv_file = STUDENTS_FOLDER + student + "/" + app +".csv"
				with open(csv_file, 'a', newline='') as csvfile:
					csvwriter = csv.writer(csvfile)
					csvwriter.writerow(row)


		key = ""
		for row in rows:
			if ('ping' in row[0]):
				continue

			app = row[3]

			for j in range(4, len(row), 4):
				if (row[j] == "rx_old"):
					break

				# data_row = row[:4] + row[j: j + 2] + row[-4:]
				data_row = row[:4] + row[j: j + 4] + row[-8:]
				# print(data_row)

				if ("Wifi" in row[2]):
					key = app + "_" + row[j] + "_wifi"
				else:
					key = app + "_" + row[j] + "_md"

				if (key not in app_count):
					app_count[key] = 0
				
				if (key not in graphs_data):
					graphs_data[key] = []
				

				if (sys.argv[1].split("-")[0] in ["apps_csv", "all", "csv"]):
					app = key.split("_")[0]
					csv_file = "./Apps/" + app + "/" + key + ".csv"
					with open(csv_file, 'a', newline='') as csvfile:
						csvwriter = csv.writer(csvfile)
						csvwriter.writerow(data_row)
						# print(date_row)

				app_count[key] += 1
				graphs_data[key] += [row[j+1]]
	

	# print(students_location)
	# print(controlled_rows)

	if ("controlled" in sys.argv[1]):
		count = -1

		csv_rows = []
		
		for i in range(len(controlled_rows)):

			count += 1
			row = controlled_rows[i]
			
			if ('ping' in row[0]):
				continue
			# print(controlled_data)
			app = row[3].lower()
			if (app not in controlled_data.keys()):
				controlled_data[app] = {}

			version = row[0]
			# print(row)
			ping_range = getPingRange(i, controlled_rows)
			print(ping_range)

			# if (len(ping_range["google"]) == 0 or len(ping_range["amazon"]) == 0 or len(ping_range["mobikwik"]) == 0):
			if (len(ping_range["google"]) == 0):
				continue

			ping_median_google = np.median(ping_range["google"])
			ping_median_amazon = np.median(ping_range["amazon"])
			ping_median_mobikwik = np.median(ping_range["mobikwik"])
			# print(ping_median_google)
			# print(row)

			n_udp = row[-1]
			n_tcp = row[-3]
			tx_old = row[-5] / 1024 / 1024
			rx_old = row[-7] / 1024 / 1024

			base_row = [app, version]

			if (rx_old == 0 or tx_old == 0):
				# print("here")
				continue

			for j in range(4, len(row), 4):
				# print("test")
				test_name = row[j]
				if (test_name == "rx_old"):
					break


				if (test_name not in controlled_data[app].keys()):
					controlled_data[app][test_name] = {}
					controlled_data[app][test_name]["latency"] = {"v1": [], "v2": [], "v3": []}
					controlled_data[app][test_name]["rx"] = {"v1": [], "v2": [], "v3": []}
					controlled_data[app][test_name]["tx"] = { "v1": [], "v2": [], "v3": [] }
					controlled_data[app][test_name]["rx_old"] = { "v1": [], "v2": [], "v3": [] }
					controlled_data[app][test_name]["tx_old"] = { "v1": [], "v2": [], "v3": [] }
					controlled_data[app][test_name]["n_udp"] = { "v1": [], "v2": [], "v3": [] }
					controlled_data[app][test_name]["n_tcp"] = { "v1": [], "v2": [], "v3": [] }
					controlled_data[app][test_name]["ping_median_google"] = { "v1": [], "v2": [], "v3": [] }
					controlled_data[app][test_name]["ping_median_amazon"] = { "v1": [], "v2": [], "v3": [] }
					controlled_data[app][test_name]["ping_median_mobikwik"] = { "v1": [], "v2": [], "v3": [] }


				latency = row[j + 1]
				rx = row[j + 2]
				tx = row[j + 3]				

				r = [app, version]
				r.extend([test_name, latency, rx, tx, rx_old,
                                    tx_old, n_tcp, n_udp, ping_median_google, ping_median_amazon, ping_median_mobikwik])

				# print('row:', r)
				csv_rows.append(r)

				controlled_data[app][test_name]["latency"][version] += [latency]
				controlled_data[app][test_name]["rx"][version] += [rx]
				controlled_data[app][test_name]["tx"][version] += [tx]
				controlled_data[app][test_name]["rx_old"][version] += [rx_old]
				controlled_data[app][test_name]["tx_old"][version] += [tx_old]
				controlled_data[app][test_name]["n_tcp"][version] += [n_tcp]
				controlled_data[app][test_name]["n_udp"][version] += [n_udp]
				controlled_data[app][test_name]["ping_median_google"][version] += [ping_median_google]
				controlled_data[app][test_name]["ping_median_amazon"][version] += [ping_median_amazon]
				controlled_data[app][test_name]["ping_median_mobikwik"][version] += [ping_median_mobikwik]

		
		# print(csv_rows)
		filename = "controlled_data_os" + str(ANDROID_OS) + ".csv"
		with open(filename, 'w', newline='') as csvfile:
			csvwriter = csv.writer(csvfile)
			# csvwriter.writerow(['APP', 'VERSION', 'FEATURE_NAME', 'LATENCY', 'RX_BYTES (MB)', 'TX_BYTES (MB)', 'RX_BYTES_OLD (MB)',
            #                 'TX_BYTES_OLD (MB)', '#TCP_CONNECTIONS', '#UDP_CONNECTIONS', 'PING_MEDIAN_GOOGLE', 'PING_MEDIAN_AMAZON', 'PING_MEDIAN_MOBIKWIK'])
			csvwriter.writerow(['APP', 'VERSION', 'FEATURE_NAME', 'LATENCY', 'RX_BYTES_OLD (MB)',
                            'TX_BYTES_OLD (MB)', '#TCP_CONNECTIONS', '#UDP_CONNECTIONS', 'PING_MEDIAN_GOOGLE', 'PING_MEDIAN_AMAZON', 'PING_MEDIAN_MOBIKWIK'])
			csvwriter.writerows(csv_rows)

			
		# print(json.dumps(controlled_data, indent=4))

		# draw_graphs_of_version_comparison(controlled_data)

	if (sys.argv[1].split("-")[0] == "pingGoogle_graphs" or sys.argv[1].split("-")[0] == "all"):
		for key in ping_graphs_data:
			fig, p1 = plt.subplots(figsize=(16,9))
			p2 = p1.twinx()

			y_values = [0]
			ping_values = []
			for row in ping_graphs_data[key]:
				y_values.append(row[0])
				ping_values.append(row[1])

			y_ticks_set = [y_tick for y_tick in range(0, max(y_values) + 500, 500)]
			x_ticks_set = []
			x_ticks_label = []

			for x_tick in range(0, len(y_values), 5):
				x_ticks_set += [x_tick]
				x_ticks_label += [str(x_tick)]
			# x_ticks_set = [x_tick for x_tick in range(0, len(y_values), 5)]

			# p1.yaxis.set_major_locator(FixedLocator(y_ticks_set))
			p1.set_ylabel("Time Taken(ms)")
			p2.set_ylabel("Ping Value")
			p1.set_xlabel("Samples")

			# print(len(y_values), len(ping_values))
			p1.plot(y_values)
			p2.boxplot(ping_values, showfliers=False)
			# print(ping_values)

			output_path = "./ping_graphs/" + key + ".png"
			plt.yticks(fontsize=8)
			plt.title(key)
			plt.tight_layout()
			plt.xticks(x_ticks_set, x_ticks_label, rotation=45)
			plt.grid()
			# plt.show()
			# break
			plt.savefig(output_path, dpi=200)

	if (sys.argv[1].split("-")[0] == "graphs" or sys.argv[1].split("-")[0] == "all"):
		for key in graphs_data:
			fig, ax = plt.subplots()
			
			# graphs_data[key] = [x for x in graphs_data[key] if x <= 30000]
			y_ticks_set = [y_tick for y_tick in range(0, max(graphs_data[key]) + 500, 500)]
			ax.boxplot(graphs_data[key])

			ax.grid()
			ax.set_yticks(y_ticks_set)
			ax.set_title(key)
			ax.set_ylabel("Time taken (ms)")

			output_path = "./graphs/" + key + ".png"
			plt.yticks(fontsize=8)
			plt.savefig(output_path, dpi=200)

	if (sys.argv[1].split("-")[0] == "find_correlation" or sys.argv[1].split("-")[0] == "all"):
		for data in correlation_matrix:
			csv_file = "./correlation_data/" + data[0] + ".csv"
			with open(csv_file, 'a', newline='') as csvfile:
				csvwriter = csv.writer(csvfile)
				csvwriter.writerow(data[1:])

	if (sys.argv[1].split("-")[0] == "jumbo" or sys.argv[1].split("-")[0] == "all"):
		# correlation_data = [cor_key, STUDENT, android_os, distance / MAX_STUDENT_DISTANCE, ram, wifi_speed, mobile_data_speed, latency, time, net_type, net_speed, ping_median, ping_min, ping_max]
		
		fields = ["FEATURE", "STUDENT", "ANDROID", "DISTANCE", "RAM", "LATENCY", "TIME_OF_DAY", "NET_TYPE", "PING_MEDIAN_GOOGLE", "PING_MEDIAN_AMAZON", "PING_MEDIAN_MOBIKWIK"]
		csv_file_1 = "./jumbo_" + sys.argv[1].split("-")[1]
		if len(sys.argv) > 2:
			csv_file_1 += "_" + sys.argv[2]
		
		csv_file_1 += ".csv"

		with open(csv_file_1, 'w', newline='') as csvfile_1:
			csvwriter1 = csv.writer(csvfile_1)
			csvwriter1.writerow(fields)
			csvwriter1.writerows(correlation_matrix)
	
	

if __name__ == "__main__":
	# Collections = MONGO_DB.list_collection_names()
	print(sys.argv[1])
	write_to_csv()
	total_tests = {"youtube":2, "linkedin":3, "hotstar":2, "gmaps":1,"gnews":1,"amazon":1, "flipkart":1, "facebook":2,"paytm":1, "mobikwik":1,"telegram":1,"whatsapp":1}
