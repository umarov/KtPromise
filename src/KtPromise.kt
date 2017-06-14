import kotlin.reflect.jvm.reflect

class KtPromise<T> {
    var resolvedValue: T? = null

    val resolver = { resolvedValue: T ->
        this.resolvedValue = resolvedValue
        resolvedValue
    }

    val rejector = { rejectedValue: T ->
        rejectedValue
    }

    constructor(promiseBody: (resolve: (anything: T) -> T, reject: (anything: T) -> T) -> Unit) {
        promiseBody(resolver, rejector)
    }

    constructor(promiseBody: (resolve: (anything: T) -> T) -> Unit) {
        promiseBody(resolver)
    }



    fun then(thenBlock: (resolvedValue: T) -> T): KtPromise<T> {
        return KtPromise({ resolve: (anything: T) -> T ->
            if (this.resolvedValue != null) {
                resolve(thenBlock(this.resolvedValue as T))
            }
        })
    }

    fun then(thenBlock: (resolvedValue: T) -> Unit) {
        if (this.resolvedValue != null) {
            thenBlock(this.resolvedValue as T)
        }
    }
}



fun main(args: Array<String>) {
    val ktPromise = KtPromise<String>({ resolve ->
        Thread.sleep(3000)
        resolve("Hello")
    })

    val upperCase = { resolvedValue: String -> resolvedValue.toUpperCase() }
    val printValue = { resolvedValue: String -> print(resolvedValue) }

    ktPromise.then(upperCase)
            .then(printValue)
}

