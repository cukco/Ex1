1. Entities
   User 
   id: Long (Primary Key)
   name: String (Not null)
   email: String (Unique)
   role: String ("ADMIN", "MEMBER")
   Task 
   id: Long (Primary Key)
   title: String (Not null)
   description: String
   status: String ("TODO", "IN_PROGRESS", "DONE")
   priority: String ("low", "medium", "high")
   userId: Long (FK)
2. Danh sách Endpoints
   Users
   Method	Endpoint	Mô tả	                                 Request Body
   GET	    /users	    Lấy danh sách toàn bộ người dùng	     N/A
   POST	    /users	    Tạo mới một người dùng	                 {"name": "...", "email": "...", "role": "..."}
   PATCH	/users/{id}	Cập nhật vai trò (role) của người dùng	 {"role": "..."}
   DELETE	/users/{id}	Xóa người dùng khỏi hệ thống	         N/A
   Tasks
   Method	Endpoint	         Mô tả	                           Request Body / Params
   GET	    /tasks	             Lấy toàn bộ danh sách công việc   N/A
   POST	    /tasks	             Tạo mới một công việc	           {"title": "...", "userId": 1, ...}
   PATCH	/tasks/{id}	         Cập nhật trạng thái công việc	   {"status": "..."}
   DELETE	/tasks/{id}	         Xóa một công việc	               N/A
   PATCH	/tasks/{id}/assignee Gắn công việc cho một người dùng  {"userId": 1}
   Filtering
   Method	Endpoint	      Mô tả	                                      Query Parameters
   GET	    /tasks	          Tìm các công việc có độ ưu tiên "high"      ?priority=high
   GET	    /tasks	          Tìm các công việc "high" của User ID 1      ?priority=high&userId=1
   GET	    /users/{id}/tasks Liệt kê toàn bộ công việc của 1 người dùng  N/A