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
    fun changeEffect(effectId: Int) = buildString {
        append("!")
        append(Command.CHANGE_EFFECT.num)
        append("0".repeat(7))
        append(effectId)
        append("$")
    }

    fun changeBrightness(brightnessLevel: Int) = buildString {
        append("!")
        append(Command.CHANGE_BRIGHTNESS.num)

        when {
            brightnessLevel < 10 -> {
                append("0".repeat(7))
            }

            brightnessLevel in 10..99 -> {
                append("0".repeat(6))
            }

            brightnessLevel > 100 -> {
                append("0".repeat(5))
            }
        }

        append(brightnessLevel)
        append("$")
    }

    fun changeFireplaceColor(colorHexString: String) = buildString {
        append("!")
        append(Command.CHANGE_FIREPLACE_COLOR.num)
        append(colorHexString)
        append("$")
    }

    fun changeFirefliesAmount(amount: Int) = buildString {
        append("!")
        append(Command.CHANGE_FIREFLIES_AMOUNT.num)
        append("0".repeat(7))
        append(amount)
        append("$")
    }

    fun changeFirefliesColorGenerationMode(enableAutoGeneration: Boolean) = buildString {
        append("!")
        append(Command.CHANGE_FIREFLIES_COLOR_GENERATION_MODE.num)
        append("0".repeat(7))
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