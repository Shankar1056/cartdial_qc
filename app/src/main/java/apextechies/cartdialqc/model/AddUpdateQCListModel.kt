package apextechies.cartdialqc.model

class AddUpdateQCListModel {

    var order_id: String?= null
    var user_id: String?= null
    var operation: String?= null
    var data: ArrayList<QcListModel>?= null
}

class QcListModel{
    var function: String?= null

    constructor(function: String?, function_status: String?) {
        this.function = function
        this.function_status = function_status
    }

    var function_status: String?= null
}