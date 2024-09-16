package com.rosahosseini.findr.startup

/**
 * The StartupTasks should be implemented by any class whose wants to execute codes for initializing
 * purposes.
 * The corresponding codes must be placed in any thread except Main.
 *
 * Examples:
 * ```
 * * val startupTasksModule = module {
 *
 *     factory {
 *          StartupTasks {
 *              Log.d("TAG", "This is a startup task!")
 *          }
 *     }
 * }
 * ```
 */
fun interface StartupTask {

    /**
     * This function will execute in [kotlinx.coroutines.Dispatchers.Default]
     */
    suspend fun run()
}
