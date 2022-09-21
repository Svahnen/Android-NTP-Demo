# Android-NTP-Demo
This is a DEMO app that fetches the current time from an NTP server and displays it on the screen.

The app gets NTP time from apache commons. <br>
When the app starts its set to Offline and will use systemtime, when you press the button it will try to get the time from the NTP server. <br>
If no internet connection is available the button will change to "No internet" when pressed, if the connection is available it will change to "Online" and the time will be updated from the NTP server. <br>
Its possible to change between the two modes by pressing the button. <br>

The app updates the time every 5 seconds using a Thread and Handler.