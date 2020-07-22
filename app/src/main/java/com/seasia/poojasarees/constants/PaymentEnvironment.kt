package com.seasia.poojasarees.constants

enum class PaymentEnvironment {
    SANDBOX {
        override fun merchant_Key(): String {
            return "IStEeyvK"
        }

        override fun merchant_ID(): String {
            return "7167910"
        }

        override fun furl(): String {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php"
        }

        override fun surl(): String {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php"
        }

        override fun salt(): String {
            return "19ncJ0Wc6X"
        }

        override fun debug(): Boolean {
            return true
        }
    };
/*,
    PRODUCTION {
        override fun merchant_Key(): String {
            return "QylhKRVd"
        }

        override fun merchant_ID(): String {
            return "5960507"
        }

        override fun furl(): String {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php"
        }

        override fun surl(): String {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php"
        }

        override fun salt(): String {
            return "seVTUgzrgE"
        }

        override fun debug(): Boolean {
            return false
        }
    };
*/

    abstract fun merchant_Key(): String?
    abstract fun merchant_ID(): String?
    abstract fun furl(): String?
    abstract fun surl(): String?
    abstract fun salt(): String?
    abstract fun debug(): Boolean
}