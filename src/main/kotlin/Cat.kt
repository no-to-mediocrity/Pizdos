
class Cat(var weight: Double, var health: Int, var age: Int) {
    var money = 0
    var image = 0
    var sick = false
    var alive = true
    var working = false
    var resting = false
    var toilet = false
    var message = ""
    var actioncount = 0
    var cycle = 100

    fun eat() {
        image = 6
        weight += 0.3
    }

    fun go_to_toilet() {
        image = 5
        weight -= 0.2
    }

    fun rest() {
        image = 3
        if (health < 100) {
            health += 1
        }
    }

    fun get_sick() {
        image = 1
        health -= 2
    }

    fun grow() {
        age += 1
        weight -= 0.5
    }

    fun work() {
        image = 2
        weight -= 0.1
        if ((0..1).random() == 0) {
            health -= 1
        }
        money += 1
    }
}