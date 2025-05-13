# Socratic: Type-Safe Paper Review System

## Contributors

1. Zaiwen Yang

   The primary designer of the architecture of the project, whose main work involved completing the development of the User microservice and Task microservice. He were also responsible for key salting, hash processing, and password modification in the UserManagement microservice, as well as the beautification of the UI interface.

2. Chenghan Yang

   Tthe lead developer for the frontend, who completed the entire frontend structure and were responsible for the UI style design.

3. Siyuan Liu

   The other backend developer, whose main work involved developing the UserManagement, Superuser, Editor, and Manager microservices. He also completed the forum system (which was later integrated into the Task microservice).

## Main Type

1. User

   A user can submit manuscript, respond to the review and join discussions on others' paper.

2. Task

   A task means the whole process of the paper review, which is the most crucial type of the system.

3. Editor

   An editor is reponsible for the decision of the papar review and the management of a periodical

## Architecture

### User Management

The UserManagement microservice centralizes the management of login and registration for all types of users. It also implements key changes, encryption, and salting.

When a user registers, the frontend sends the user's information and key to the microservice corresponding to the user type in the backend. The corresponding microservice locally completes key salting and encryption, saves it within that microservice, and then initiates an approval request to the higher-level microservice (User doesn't require approval, Editor needs Manager approval, Manager needs Superuser approval). 

After approval, the higher-level microservice notifies the lower-level microservice that registration can begin. At this point, the lower-level microservice sends the user type, encrypted key, and salt to the UserManagement microservice to complete the registration.

When a user logins, the frontend only need to send the user name and password to the UserManagement miocroservice to check the validation.

For the sake of limited time and man power (We are the only team of just 3 men), it is a pity that we didn't support the token mechanism, which is strongly recommended by Professor Yuan Yang. However, our system is complex enough (Many pages and functionality!) and has a very beautiful interface, which is hopefully a remedy to this.

### User Information Collection

Our system support basic information, profile photo setting and edition. 

When users register, they are required to fill in their relevant personal information. This information set is stored in the corresponding microservice. Users can modify their information on their personal homepage. The backend provides simple APIs for the frontend to call and retrieve this information. 

### Task Submission

The main part of our system is about tasks, and here is the beginning.

A user can submit their article and fill in related information. After this information is sent to the backend, it will be stored, and a Task with a status of "Init" will be generated. After the initial review by an Editor, the Task will enter the "Review" status, which means it enters the peer review stage. User can also view their task at "My tasks" Page and add coauthor there.

### Task Review

The core part of our project is here.

First, we introduce the concepts of journals and Reviewers. All articles must be submitted to corresponding journals. Journals can be added by Managers. When Editors register, they must apply to become an editor for a specific journal, which is then reviewed by a Manager. After a successful application, an Editor can add up to 10 Users as Reviewers for that journal. When a Task enters the Review status, it is randomly assigned to 3 Reviewers. These Reviewers will see the corresponding Task displayed on their "My Task " page. (contributed by 刘思远)

Next, we introduce the comment system. Each Task has a homepage where anyone can view information about the Task and download the paper PDF. Comments are divided into three types: Comment (initiated by any user), Review (formal review by a Reviewer), and Rebuttal (author's response to Comments and Reviews). The Editor will make a final decision on whether to accept the paper based on this content. (contributed by 刘思远)

Then, we explain the anonymity system. Peer review is conducted blindly, so on the Task homepage and in comments, each person is associated with a pseudonym. These pseudonyms are assigned when the author submits, when Reviewers are assigned, or when a user comments for the first time.

Finally, the editor make a decision that whether a paper is accepted, revision-needed, or rejected, and change the paper into the relevant state. After that, only comment can be made.

### Browse System

Finally, to allow everyone to comment on a paper, we have created a Browse System. Anyone can search for relevant articles by paper title or journal, and then enter the article's homepage to participate in the discussion.

## Even More Detailed Backend API

**Superuser**: This microservice is responsible for managing administrators. Its main functionalities include:

1. **AuthenManagerMessagePlanner**: Receives Manager.ManagerRegisterMessage and stores the application in the database.
2. **ReadTasksMessagePlanner**: Retrieves all pending applications and displays them on the Superuser frontend.
3. **FinishManagerMessagePlanner**: Processes a Manager application (approved/denied), removes the corresponding task from the database, and sends a Manager.ManagerRequestMessage.

**Manager**: This microservice handles journal management and Editor registration reviews. Its main functionalities include:

1. **ManagerRegisterMessagePlanner**: Temporarily stores manager information upon receiving a Register request and forwards it to Superuser.AuthenManagerMessagePlanner.
2. **ManagerRequestMessagePlanner**: After receiving a Superuser.FinishManagerMessage, if the application is approved, it sends usermanagement.RegisterMessage; otherwise, it keeps the information locally.
3. **AddPeriodicalMessagePlanner**: Allows the Manager to manually add new Periodicals.
4. **ReadPeriodicalMessagePlanner**: Retrieves all current Periodicals for frontend display.
5. **AuthenEditorMessagePlanner, ReadTasksMessagePlanner, FinishEditorMessagePlanner**: Similar to the Superuser microservice, these handle Editor registration applications.

**Editor**: This microservice manages Editor personal information, initial and final paper reviews, and adds reviewers to journals. Its main functionalities include:

1. **EditorRegisterMessagePlanner, EditorRequestMessagePlanner**: Similar to Manager, these handle Editor registration and pending reviews.
2. **AddReviewerMessagePlanner, DeleteReviewerMessagePlanner**: Manage adding and removing Reviewers for the Editor’s journal.
3. **AllocateReviewerMessagePlanner**: After receiving User.UserSubmissionMessagePlanner, selects three random Reviewers from the corresponding journal’s Reviewers for the paper and sends Task.AddTaskIdentityMessagePlanner.
4. **EditorEditProfilePhotoMessagePlanner, UserEditProfilePhotoMessagePlanner, UserReadProfilePhotoMessagePlanner**: Manage the display and updating of Editor profile pictures.
5. **EditorReadInfoMessagePlanner**: Handles queries for Editor information.

**User**: This microservice manages user personal information, paper submissions, and participation in reviews. Its main functionalities include:

1. **UserRegisterMessagePlanner**: Manages user registration.
2. **UserReadInfoMessagePlanner, UserEditProfilePhotoMessagePlanner, UserReadProfilePhotoMessagePlanner**: Similar to Editor, these handle user information queries and profile picture management.
3. **UserSubmissionMessagePlanner**: Processes paper submission requests and forwards them to Task.TaskQueryMessage and Editor.AllocateReviewerMessagePlanner.

**Task**: This task system stores and maintains paper and review information. Its main functionalities include:

1. **AddTaskIdentityMessagePlanner**: Adds user permissions (Author, Reviewer, comment) to specific papers and assigns pseudonyms for anonymous comments.
2. **CheckTaskIdentityMessagePlanner**: Retrieves user permissions for a specific paper to assign review types on the frontend.
3. **TaskQueryMessagePlanner**: Stores paper information and files in the database, and assigns author permissions.
4. **TaskEditInfoMessagePlanner, ReadTaskPDFMessagePlanner, ReadTaskListMessagePlanner, ReadTaskInfoMessagePlanner,ReadTaskAuthorMessagePlanner,ReadPeriodicalTaskListMessagePlanner**: Handle modifications and various queries for paper information.
5. **SearchTaskMessagePlanner**: Searches for specific paper titles for user comments, supporting fuzzy search.
6. **ReadAliasMessagePlanner, ReadAliasTokenMessagePlanner**: Supports reading pseudonyms and independent tokens for anonymous comments.
7. **AddLogMessagePlanner**: Adds logs in three different formats based on user permissions.
8. **ReadLogListMessagePlanner**: Queries all logs for a specific paper, sorted by time.
9. **AddRebuttalMessagePlanner**: Allows authors to provide a rebuttal for each comment.

**UserManagement**: This microservice manages user registration, login, and permission assignments. Its main functionalities include:

1. **RegisterMessagePlanner**: Stores usernames, passwords, and permissions for various users who have been approved.
2. **LoginMessagePlanner**: Supports login for all types of users.

**CheckUserExistsMessagePlanner**: Checks for existing users with the same name among all users to ensure no name conflicts during registration.

## Highlight of our project

1. **Type Safety**: The system uses a strict type-checking mechanism to ensure data accuracy and consistency, reducing errors and vulnerabilities.
2. **Microservices Architecture**: Each module of the system is independently deployed, operated and maintained by git submodule, offering high scalability and maintainability. In this way, we can collaborate parallel.
3. **End-to-End Management**: The system covers the entire process from adding and reviewing journals and editors to paper submission, review, comments, rebuttals, and final decisions, ensuring the completeness and fairness of the paper review process.
4. **Anonymous Comments**: Regular users can leave anonymous comments, enhancing the authenticity and diversity of the feedback. For the sake of time, we didn't show the real name after a paper is rejected of accepted, which is a shame.
5. **Multi-role Collaboration**: The system supports collaboration among multiple roles, including     superusers, administrators, editors, reviewers, and regular users. Each role has clear functions, ensuring efficient collaboration.
6. **Safe Key Management:** We use salting and hashing to maintain the key at the backend, which very safe.

*Socratic is dedicated to providing a fair, efficient, and type-safe paper review platform, helping the academic community improve the quality and efficiency of paper reviews.*
