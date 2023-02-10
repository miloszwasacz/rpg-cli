fun setupLogger() {
    //TODO Add better logger
    System.setProperty(org.slf4j.simple.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE")
    System.setProperty(org.slf4j.simple.SimpleLogger.LOG_FILE_KEY, "System.out")
    System.setProperty(org.slf4j.simple.SimpleLogger.SHOW_LOG_NAME_KEY, "false")
    System.setProperty(org.slf4j.simple.SimpleLogger.SHOW_THREAD_NAME_KEY, "false")
    System.setProperty(org.slf4j.simple.SimpleLogger.LEVEL_IN_BRACKETS_KEY, "true")
}