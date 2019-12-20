package com.wzg.core.model

import java.io.Serializable


/**
 *  @author  bulus
@date  2018/8/1
 */


class LoginResponseModel : Serializable {
    var companyCode: String? = null // 0207
    var deptCode: String? = null // 00000000_NresponseuSBQTA
    var deptId: String? = null // 1052151165193437184
    var deptName: String? = null // 指旺管理部
    var hashCode: Int? = null // 1437856423
    var realName: String? = null // 系统管理员
    var roleIds: ArrayList<String>? = null
    var token: String? = null // 9f3ce34f-7e29-45cb-8eb9-f75dde2e12a7
    var userId: String? = null // 1052152058794098688
    var avatar: String? = null // 1052152058794098688
    var userName: String? = null // administrator
}