package apextechies.cartdialqc.model

class DeviceQuestionModel {

    var id: String?= null

    constructor(id: String?, function_name: String?, status: String?) {
        this.id = id
        this.function_name = function_name
        this.status = status
    }

    var function_name: String?= null
    var status: String?= null
    var isAnswered: Boolean = false
    var checkedId = -1
    var selectedText = ""
}