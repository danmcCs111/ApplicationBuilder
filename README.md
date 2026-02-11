Install git with git bash.

https://git-scm.com/

To build sqlite in **Windows** included scripts are using **visual studio community install**

With "Desktop development with C++" package. seen in checkbox below. (buildLite.cmd in my sqlite project is using to build)

<img width="75%" width="1263" height="711" alt="image" src="https://github.com/user-attachments/assets/54f20ee9-8fb9-4c52-9e12-6b31b73d7c0a" />



In the drive and directory desired

(Recommend saving on a seperate external SSD for portability of the app as well as files accumulated and database entries)

Run:

    git clone https://github.com/danmcCs111/ApplicationBuilder.git

In the project:

Run:

    installAndUpdate.sh

Run:

    mavenBuild.sh


(using in a dual monitor setup.)

<img width="45%" src="https://github.com/user-attachments/assets/a5dc474a-e387-48ec-9ecb-7680b0b8e732" />

*seperated view and update

*switched to a tree structure in layout editor

*switched to a local sqlite install keeping database contents within project directory

*Added launching of database service in launch menu for video launching app

*Added youtube api lookup (requiring key), database table and lookup for popup. adds based on time lapsed since last viewed.

projects required are contained in plugin-projects folder now. 

Added an Edit collection dialog in Action Menu (drag and drop url support also replicated into dialog)
<p float="left">
<img width="45%" alt="popup" src="https://github.com/user-attachments/assets/25b23245-0c20-4517-9cbb-1cd0ff9ad636" />
<img width="45%" height="497" alt="remove" src="https://github.com/user-attachments/assets/32ae2127-f5a0-44e2-abc2-ebb1c229770c" />
<img width="45%" height="586" alt="tree-structure" src="https://github.com/user-attachments/assets/617688ee-b6e8-4200-ad2d-a090d1ff1998" />
<img width="45%" width="1057" height="439" alt="right_click-final" src="https://github.com/user-attachments/assets/3b1b81bd-be2e-49c8-bc4e-7c41269e8fec" />
</p>


*fixed browser process to be seperate profile. switched to using PID and sharing with ahk to give direct key press control.

added drag and drop capability with an editable page parser

<img width="1631" height="590" alt="page-parser" src="https://github.com/user-attachments/assets/b3ea2bc8-72e1-419d-b1ef-c5da89cc7bd1" />


added roku videos. changed scaling to only use width and keep aspect ratio of image.
<img width="1940" height="986" alt="image" src="https://github.com/user-attachments/assets/44e405cc-9eb3-4790-862b-e06513c578b2" />


added Frame shift. new save/open dialogs. 
java swing filechooser left as an option. 
fixed path settings for linux save/open dialogs.
added additional settings to layout. (different picture sizes for preview/open window, turn off preview, remove title text on images)

<img width="1746" height="1366" alt="shift-add3" src="https://github.com/user-attachments/assets/bbb4bd47-72f5-4b61-9059-050edb8e173a" />


Adjusted UI styling: Title UI Text, startup Locations, and color. 
added search and title to list creator. adjusted reload option to just launch the scheduled command list editor/launcher. added enter key detect for search bar.

<img width="1864" height="1063" alt="black-ui-adjust3" src="https://github.com/user-attachments/assets/de828b4f-b790-4e69-a576-d5967258ff0d" />


Loading -> graphic & text / Text only
<p float="left">
<img alt="Screenshot 2025-11-13 142500" src="https://github.com/user-attachments/assets/02c8cac5-feca-49bb-a802-44ee74e8b7c1" width="45%" />
<img align="Top" alt="loading added" src="https://github.com/user-attachments/assets/03cff1e9-c63f-4cb8-9b47-19e6716a512a" width="45%" />
</p>

Add seperate menu
<img width="429" height="986" alt="image" src="https://github.com/user-attachments/assets/547bba80-feef-4719-9e85-dd935afa2d57" />


*adjusted paths to relative ones to support linux

<img width="1910" height="1192" alt="image" src="https://github.com/user-attachments/assets/566945ba-34a4-41c9-85b8-79439ab0c7fd" />

* added video list visual selection creator

<img width="1897" height="1130" alt="Screenshot 2025-09-08 204044" src="https://github.com/user-attachments/assets/03053c18-1b38-4945-9c87-8bf4ae144cdf" />

<img width="3434" height="1428" alt="image" src="https://github.com/user-attachments/assets/88d3ecb1-0ee7-4663-be15-70fdce0e638a" />



OLDER:

![video_launch](https://github.com/user-attachments/assets/1a8eff64-7255-44c6-b4c2-283b87ca014a)


![video_output](https://github.com/user-attachments/assets/1bd73912-4c0f-47c5-964e-ade74c5ab094)

![image_builder](https://github.com/user-attachments/assets/bef1a429-8505-4946-82d6-2f13d8e3237b)

![example_collection](https://github.com/user-attachments/assets/497d46c3-a491-418a-8095-14ad6ca39aeb)

![editor_file_directory](https://github.com/user-attachments/assets/03faf10f-9b9f-4a96-a454-ad3d3e145af7)


![database query tool](https://github.com/user-attachments/assets/2a5f77c3-dcf8-4881-9a19-a38a449ce763)


![shape_creator](https://github.com/user-attachments/assets/5545b995-672f-464e-982a-f126eae909f3)
