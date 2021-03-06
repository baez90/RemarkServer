# Remark Server

This is a small Kotlin application which is based on [remark](https://remarkjs.com/).
It runs a Vertx based web server and brings a little bit more comfort to remarkjs.

You don't have to write an HTML page where your presentation is embedded but enables you to write a pure Markdown presentation by generating the HTML website for you.
To enable you to style your presentation the remark server creates a `slides-style.css` next to your specified markdown file.
Remark server is monitoring both the markdown and the css file for changes and regenerates an `index.html` everytime a change occurs.
Just reload your Browser and get the changes!

# Setup

To get it up, specify the following parameters:

* -w / --workingDir to a writable directory where index.html and remark.js files where be placed and delivered from by the web server
* -m / --markdownFile path to the markdown file containing your presentation
* -p / --port port which will be used by the web server

Afterwords go on and edit your markdown presentation and the slides-style.css in the directory you entered by the markdown file path. Everytime you save the .css or the .md file the index.html file will get regenerated by application
