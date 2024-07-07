package Global

import Global.GlobalVariables.serviceCode
import cats.effect.IO
import com.comcast.ip4s.Port
import org.http4s.Uri

object ServiceCenter {
  val projectName: String = "APP"
  
  val superuserServiceCode = "A000007"
  val dbManagerServiceCode = "A000001"
  val doctorServiceCode    = "A000002"
  val patientServiceCode   = "A000003"
  val portalServiceCode    = "A000004"
  val managerServiceCode   = "A000008"
  val editorServiceCode    = "A000009"
  
  val fullNameMap: Map[String, String] = Map(
    dbManagerServiceCode ->  "数据库管理（DB_Manager）",
    doctorServiceCode    ->  "医生（Doctor）",
    patientServiceCode   ->  "病人（Patient）",
    portalServiceCode    ->  "门户（Portal）" ,
    superuserServiceCode ->  "超级用户（Superuser）" ,
    managerServiceCode   ->  "管理员（Manager）",
    editorServiceCode    ->  "编辑（Editor）"
  )

  val address: Map[String, String] = Map(
    "DB-Manager" ->     "127.0.0.1:10001",
    "Doctor" ->         "127.0.0.1:10002",
    "Patient" ->        "127.0.0.1:10003",
    "Portal" ->         "127.0.0.1:10004",
    "Superuser" ->      "127.0.0.1:10007",
    "Manager"->         "127.0.0.1:10008",
    "Editor"->          "127.0.0.1:10009"
  )
}
