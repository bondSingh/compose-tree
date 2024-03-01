package img.tree.network

import img.compose_tree.R

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

enum class ERROR_TYPE {
    NO_NETWORK {
        override fun errorMessage(): Int {
            return R.string.no_internet_connection
        }
    },
    SERVER_ERROR {
        override fun errorMessage(): Int {
            return R.string.server_error
        }
    },
    GENERIC_ERROR {
        override fun errorMessage(): Int {
            return R.string.server_error
        }
    },
    EMPTY_RESPONSE {
        override fun errorMessage(): Int {
            return R.string.empty_response
        }
    },
    NOT_FOUND {
        override fun errorMessage(): Int {
            return R.string.records_not_found
        }
    };
    abstract fun errorMessage(): Int
}