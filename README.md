# Mobile_Computing_Assignments
All  Assignments done in Mobile Computing course in Indraprastha Institute of Information Technology, Delhi (IIITD).

## Table Of Contents

|S.NO|                               TOPICS                                                 | PROJECT NAME      |
|----|--------------------------------------------------------------------------------------|-------------------|
|01. | Basics of Android : **Activity, Intent and Log and Rotation Handling** |[001_Android_Activity](001_Android_Activity/)      |
|02. |Basic Music Application using : **fragmmet , service (foreground witn notifications), Broadcast Receivers** for complete application, Check Connection, **Async Task to download song internal to the app** , Play songs from raw and internal files     |[002_Service_Broadcast-Receiver_and_Async-Task](002_Service_Broadcast-Receiver_and_Async-Task/)     |                                          
|03. | Displaying and Editing list of students using **Recycler View, Adapter (No Databse)**   |[003_Recyclerview_and_Fragments](003_Recyclerview_and_Fragments/)  |
|04. |Recording real time **sensor** data and storing in **Room database** while retrieving mean of data till date |[004_Android_Sensors_and_Room_Database](004_Android_Sensors_and_Room_Database/)  |
|05. | **Indoor Localization** using wifi with room level accuracy : access wifi available, record RSI level, Localize using **Wardriving and KNN**,also Plotting data in android  |[005_Indoor_Localization_using_Wifi](005_Indoor_Localization_using_Wifi/)       |


## Outputs 

## 001_Android_Activity

                                   Fig 1. Basic Android Activity and Intents usage 

<div align="center"><img src="Images/A1.gif" height='250px'/></div>


## 002_Service_Broadcast-Receiver_and_Async-Task

                                   Fig 1. Music Application with play and pause functionality (foreground service) and download file using async task 

<div align="center"><img src="Images/A2.gif" height='250px'/></div>

## 003_Recyclerview_and_Fragments

                                   Fig 1. Recycler View in Android


<div align="center"><img src="Images/A3.gif" height='250px'/></div>

## 004_Android_Sensors_and_Room_Database

                                   Fig 1. Recoding real time data from sensors and checking mobility 


<div align="center"><img src="Images/A4.gif" height='250px'/></div>

## 005_Indoor_Localization_using_Wifi

- Implemented Localization using Wardriving with wifi 
- Collect Inital data for Wardriving
    - Detect wifi devices to record RSI values from them. 
    - Collect training data for wardriving i.e which room receives what RSI values from wifi devices near it (room level acuracy)
    . Using these we know room1 receives [x1,x2,x3] rsi values for wifi [y1,y2,y3] respectively. if any prediction point [z1,z2,z3] is close to [x1,x2,x3] for same wifi devices [y1,y2,y3] then we can predict the location to be room1.  
    - For finding location simply we find eculidean distances and use k nearest neighbour to localize the room of the user.  


                                   Fig 1. Indoor Localization using Wifi  


 <table>
  <tr>
    <td>Available wifi list with RSI values</td>
     <td>Wardriving data collection with selected wifi devices</td>
     <td>Prediction using KNN</td>
     <td>Wifi Scanner using Barplots</td>
  </tr>
  <tr>
    <td><img src="Images/A5_wifi_rsi_values.gif" width=270 height=300></td>
    <td><img src="Images/A5_wardriving_data_collection.gif" width=270 height=300></td>
    <td><img src="Images/A5_wardrive_prediction.gif" width=270 height=300></td>
    <td><img src="Images/A5_wifi_scanner.gif" width=270 height=300></td>
  </tr>
 </table> 