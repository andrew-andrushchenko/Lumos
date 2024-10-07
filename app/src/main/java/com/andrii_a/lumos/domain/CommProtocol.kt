package com.andrii_a.lumos.domain

private enum class Command(val num: Int) {
    CHANGE_EFFECT(0),
    CHANGE_BRIGHTNESS(1),
    CHANGE_FIREPLACE_COLOR(2),
    CHANGE_FIREFLIES_AMOUNT(3),
    CHANGE_FIREFLIES_COLOR_GENERATION_MODE(4),
    CHANGE_FIREFLY_COLOR(5)
}

object CommProtocol {
    fun changeEffect(effectId: Int): String {
        return "!${Command.CHANGE_EFFECT.num};$effectId$"
    }

    fun changeBrightness(brightnessLevel: Int) : String {
        return "!${Command.CHANGE_BRIGHTNESS.num};$brightnessLevel$"
    }

    fun changeFireplaceColor(colorHexString: String): String {
        return "!${Command.CHANGE_FIREPLACE_COLOR.num};$colorHexString$"
    }

    fun changeFirefliesAmount(amount: Int): String {
        return "!${Command.CHANGE_FIREFLIES_AMOUNT.num}${"0".repeat(6)}$amount$"
    }

    fun changeFirefliesColorGenerationMode(enableAutoGeneration: Boolean) = buildString {
        append("!")
        append(Command.CHANGE_FIREFLIES_COLOR_GENERATION_MODE.num)
        append("0".repeat(6))
        append(if (enableAutoGeneration) 1 else 0)
        append("$")
    }

    fun changeFireflyColor(index: Int, colorHexString: String) = buildString {
        append("!")
        append(Command.CHANGE_FIREFLY_COLOR.num)
        append(index)
        append(colorHexString)
        append("$")
    }
}