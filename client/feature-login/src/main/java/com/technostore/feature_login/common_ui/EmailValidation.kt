package com.technostore.feature_login.common_ui

const val EMAIL_REGEX =
    "(^|\\(|:)[a-zA-Z]+([-|\\.]?[a-zA-Z0-9])*@[a-zA-Z0-9]+([-|\\.]?[a-zA-Z0-9])*\\.[a-zA-Z]+(\\s|\\b|$|\\,|\\?)"

enum class EmailValidation {
    SUCCESS,
    ERROR_MAX_LENGTH,
    ERROR,
    EMPTY,
    EXISTS,
    NOT_EXISTS
}