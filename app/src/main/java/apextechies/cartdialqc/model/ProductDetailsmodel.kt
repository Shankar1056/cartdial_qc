package apextechies.cartdialqc.model

class ProductDetailsmodel {

    var order_item_type: String? = null
    var order_item_name: String? = null
    var product_price: ArrayList<ProdPrice>? = null

    constructor(order_item_type: String?, order_item_name: String?, product_price: ArrayList<ProdPrice>?) {
        this.order_item_type = order_item_type
        this.order_item_name = order_item_name
        this.product_price = product_price
    }
}

class ProdPrice {

    var meta_key: String? = null
    var meta_value: String? = null

    constructor(meta_key: String?, meta_value: String?) {
        this.meta_key = meta_key
        this.meta_value = meta_value
    }
}
