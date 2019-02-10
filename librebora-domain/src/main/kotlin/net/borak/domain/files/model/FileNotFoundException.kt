package net.borak.domain.files.model

import java.lang.RuntimeException

class FileNotFoundException(message: String) : RuntimeException(message)