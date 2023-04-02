import java.util.*
import kotlin.concurrent.fixedRateTimer
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.system.*

fun main() {
    val cat = Cat(5.0, 100, 0)
    fixedRateTimer("timer", true, 0L, 1000L) {
        displayMenu(cat)
        updateCat(cat)
    }
    val console = System.console()
    if (console != null) {
        while (true) {
            getInput(String(console.readPassword()),cat)
        }
    } else {
        val scanner = Scanner(System.`in`)
        val input = scanner.nextLine()
        getInput(input,cat)
    }
}

fun getInput(input: String, cat: Cat) {
    if (cat.alive == true) {
        when (input) {
            "1" -> {
                if (cat.money >= 2) {
                    cat.money -= 2
                    cat.eat()
                    cat.message = "The cat has been fed!"
                } else {
                    cat.message = "Not enough money to eat!"
                }
            }

            "2" -> {
                if (cat.money >= 5) {
                    cat.money -= 5
                    if (cat.health + 10 >= 100) {
                        cat.health = 100
                    } else {
                        cat.health += 10
                    }
                    cat.sick = false
                    cat.message = "The cat has been given medicine!"
                } else {
                    cat.message = "Not enough money to buy the medicine!"
                }
            }

            "3" -> {
                cat.actioncount = 0
                cat.resting = true
                cat.message = "The cat is resting!"
            }

            "4" -> {
                if (cat.sick == false) {
                    cat.working = true
                    cat.message = "The cat went to work!"
                } else {
                    cat.message = "Can't go to work when sick!"
                }
            }

            "5" -> exitProcess(0)
        }
    } else {
        when (input) {
            "5" -> exitProcess(0)
        }
    }
    // Process the user's input here
}



fun displayMenu(cat: Cat){
    print("\u001b[2J\u001b[H")
    val ascii = arrayListOf(catascii,catascii_sick,catascii_work, catascii_rest, catascii_dead, catascii_toilet, catascii_eating) // 0 - normal cat, 1 - sick, 2 - working, 3 - resting, 4 - dead

    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    // Print the cat ASCII art
    println(ascii[cat.image])
    // println("debug (resting): ${cat.resting}, image: ${cat.image}, actioncount:${cat.actioncount}"
    // Print the cat's status
    println("   Name: Pizdos")
    println("   💵 Bank balance: ${cat.money}$")
    println("   👴 Age: ${cat.age} ⚖️  Weight: ${(df.format(cat.weight))} kg ❤️  Health: ${cat.health}")
    println("")
    if (cat.alive == true) {
        println("   Choose an action (type the number, then press Enter):")
        println("   1. Feed the cat 🐟")
        println("   2. Give medicine 💊")
        println("   3. Let the cat rest 😴")
        println("   4. Go to work 💵")
        println("   5. Exit 🛑")
        println("")
        println("   ${cat.message}")
    } else {
        println("")
        println("   ${cat.message}")
    }
}

fun updateCat(cat: Cat){
    if (cat.alive) {
        if (cat.cycle == 0) {
            cat.cycle = 100
            cat.grow()
        }
        if (cat.age == 25) {
            if ((0..100).random() == 0) {
                cat.alive = false
                cat.message = "The cat has died because of the old age.\n  Type \"5\" and then press Enter to exit."
            }
        }
        if (cat.weight >= 12){
            if ((0..10).random() == 0) {
                cat.health -= 5
            }
        }
        if (cat.health <= 0) {
            cat.alive = false
            cat.message = "The cat has died because its health reached zero.\n   Type \"5\" and then press Enter to exit."
        }
        if (cat.weight <= 2) {
            cat.alive = false
            cat.message = "The cat has died because of starvation.\n   Type \"5\" and then press Enter to exit."
        }
        if (cat.weight >= 15) {
            cat.alive = false
            cat.message = "The cat has died because it was too obese.\n   Type \"5\" and then press Enter to exit."
        }
        // Check if the cat randomly gets sick and stops working
        if ((0..250).random() == 0) {
            cat.message = "The cat got sick!"
            cat.sick = true
            cat.working = false
            cat.resting = false
            cat.actioncount = 0
        }

        // Check if the cat randomly poops
        if (((0..100).random() == 0) && !cat.sick && !cat.working) {
            cat.message = "The cat is pooping!"
            cat.actioncount = 0
            cat.toilet = true
        }
        // Update cat's status based on current action
        when {
            cat.working -> {
                // Update cat's status while working
                if (cat.actioncount < 10) {
                    cat.work()
                    cat.actioncount++
                } else {
                    cat.actioncount = 0
                    cat.working = false
                }
            }
            cat.sick -> {
                // Update cat's status while sick
                if (cat.actioncount < 15) {
                    cat.get_sick()
                    cat.actioncount++
                } else {
                    cat.sick = false
                    cat.actioncount = 0
                }
            }
            cat.resting -> {
                // Update cat's status while resting
                if (cat.actioncount < 5) {
                    cat.rest()
                    cat.actioncount++
                } else {
                    cat.resting = false
                }
            }
            cat.toilet -> {
                if (cat.actioncount < 3) {
                    cat.go_to_toilet()
                    cat.actioncount++
                } else {
                    cat.actioncount = 0
                    cat.toilet = false
                }
            }
            else -> {
                cat.image = 0
                cat.message = ""
            }
        }
    } else {
        cat.image = 4
    }
    cat.cycle--
}