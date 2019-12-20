package com.wzg.core.model

import java.io.Serializable

/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/5/30
 * 修改时间:  2018/5/30
 * 修改备注:  说明本次修改内容
 */
class UserModel : Serializable {
    var token: String? = null
    var url: String? = null
    var personalId: String? = null
    var personalName: String? = null
    var roldId: String? = null
    var deptId: String? = null
    var deptName: String? = null
    var userRole: ArrayList<UserRole>? = null

    class UserRole : Serializable {
        var roleId: Int? = null//" : 4,
        var roleName: String? = null//" : "一审人员"
    }
}
