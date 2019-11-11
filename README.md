# YourPlace

Loading data from server as JSON file using only `java.net.Socket` and displaying it as endless recycler view. Each element of recycler view are clickable and opens in new activity as layout of text views and marked google map using longitude and latitude data from loaded JSOn file. 

Here is the simple feature description of each activity:

### Login Activity

- Sending GET request at login button click to http://www.alarstudios.com/test/auth.cgi with passing password and login using `java.net.Socket`. 
- If response code is ok then starting main activity with passing code from response.
- Showing Toast message in case of wrong login or password.
- Showing error in `EditText` fields if they are empty at button click.

### Main Activity

- Recycler view with elements loaded from server by sending GET request to http://www.alarstudios.com/test/data.cgi with passing code from previous activity and page number using `java.net.Socket`. 
- Every time there is an addition of new elements if there is a scroll to the end of recycler view.
- Asynchronous image loading for each element of recycler view.
- Each element is clickable and opens new activity with passing params for this element from JSON file.

### Map Activity

- Displays data passed from previous Activity as `TextView`s
- Displays google map with marker on it for some place using longitude and latitude from JSON file.
- Has back button on top for getting back to the previous activity.

### Results

![ezgif com-video-to-gif](https://user-images.githubusercontent.com/14878297/68557170-154fe200-0446-11ea-8193-da7889233ba0.gif)
