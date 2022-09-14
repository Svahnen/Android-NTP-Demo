# Android-NTP-Demo
This is a DEMO app that fetches the current time from an NTP server and displays it on the screen.

I believe in writing code that is self explanatory and easy to understand. So I have not added that many comments in the code.
If you have any questions, please feel free to ask.

The app gets NTP time from apache commons
When the app starts its set to Offline and will use systemtime, when you press the button it will try to get the time from the NTP server.
If no internet connection is available the button will change to "No internet" when pressed, if the connection is available it will change to "Online" and the time will be updated from the NTP server.
Its possible to change between the two modes by pressing the button.

The app updates the time every 5 seconds using a Thread and Handler.