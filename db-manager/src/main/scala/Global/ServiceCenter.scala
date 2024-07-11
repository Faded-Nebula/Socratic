package Global

import Global.GlobalVariables.serviceCode
import Global.ServiceCenter.usermanagementServiceCode
import cats.effect.IO
import com.comcast.ip4s.Port
import org.http4s.Uri

object ServiceCenter {
  val projectName: String = "APP"

  val dbManagerServiceCode = "A000001"
  val doctorServiceCode    = "A000002"
  val patientServiceCode   = "A000003"
  val portalServiceCode    = "A000004"
  val userServiceCode      = "A000005"
  val taskServiceCode      = "A000006"
  val superuserServiceCode = "A000007"
  val managerServiceCode   = "A000008"
  val editorServiceCode    = "A000009"
  val usermanagementServiceCode    = "A000010"

  val fullNameMap: Map[String, String] = Map(
    dbManagerServiceCode ->  "数据库管理（DB_Manager）",
    doctorServiceCode    ->  "医生（Doctor）",
    patientServiceCode   ->  "病人（Patient）",
    portalServiceCode    ->  "门户（Portal）" ,
    userServiceCode      ->  "用户（User）",
    taskServiceCode      ->  "任务（Task）",
    superuserServiceCode ->  "超级用户（Superuser）" ,
    managerServiceCode   ->  "管理员（Manager）",
    editorServiceCode    ->  "编辑（Editor）",
    usermanagementServiceCode -> "用户管理（UserManagement）"
  )

  val address: Map[String, String] = Map(
    "DB-Manager" ->     "127.0.0.1:10001",
    "Doctor" ->         "127.0.0.1:10002",
    "Patient" ->        "127.0.0.1:10003",
    "Portal" ->         "127.0.0.1:10004",
    "User" ->           "127.0.0.1:10005",
    "Task" ->           "127.0.0.1:10006",
    "Superuser" ->      "127.0.0.1:10007",
    "Manager"->         "127.0.0.1:10008",
    "Editor"->          "127.0.0.1:10009",
    "UserManagement" -> "127.0.0.1:10010"
  )
}
