package com.example.rolcraft.dados

import com.example.rolcraft.R

object DiceLibrary {

    val D4 = DiceType(
        name = "D4",
        faces = 4,
        images = listOf(
            R.drawable.d4_1,
            R.drawable.d4_2,
            R.drawable.d4_3,
            R.drawable.d4_4
        )
    )

    val D6 = DiceType(
        name = "D6",
        faces = 6,
        images = listOf(
            R.drawable.d6_1,
            R.drawable.d6_2,
            R.drawable.d6_3,
            R.drawable.d6_4,
            R.drawable.d6_5,
            R.drawable.d6_6
        )
    )

    val D8 = DiceType(
        name = "D8",
        faces = 8,
        images = listOf(
            R.drawable.d8_1,
            R.drawable.d8_2,
            R.drawable.d8_3,
            R.drawable.d8_4,
            R.drawable.d8_5,
            R.drawable.d8_6,
            R.drawable.d8_7,
            R.drawable.d8_8
        )
    )

    val D10 = DiceType(
        name = "D10",
        faces = 10,
        images = listOf(
            R.drawable.d10_1,
            R.drawable.d10_2,
            R.drawable.d10_3,
            R.drawable.d10_4,
            R.drawable.d10_5,
            R.drawable.d10_6,
            R.drawable.d10_7,
            R.drawable.d10_8,
            R.drawable.d10_9,
            R.drawable.d10_0
        )
    )

    val D12 = DiceType(
        name = "D12",
        faces = 12,
        images = listOf(
            R.drawable.d12_1,
            R.drawable.d12_2,
            R.drawable.d12_3,
            R.drawable.d12_4,
            R.drawable.d12_5,
            R.drawable.d12_6,
            R.drawable.d12_7,
            R.drawable.d12_8,
            R.drawable.d12_9,
            R.drawable.d12_10,
            R.drawable.d12_11,
            R.drawable.d12_12
        )
    )

    val D20 = DiceType(
        name = "D20",
        faces = 20,
        images = listOf(
            R.drawable.d20_1,
            R.drawable.d20_2,
            R.drawable.d20_3,
            R.drawable.d20_4,
            R.drawable.d20_5,
            R.drawable.d20_6,
            R.drawable.d20_7,
            R.drawable.d20_8,
            R.drawable.d20_9,
            R.drawable.d20_10,
            R.drawable.d20_11,
            R.drawable.d20_12,
            R.drawable.d20_13,
            R.drawable.d20_14,
            R.drawable.d20_15,
            R.drawable.d20_16,
            R.drawable.d20_17,
            R.drawable.d20_18,
            R.drawable.d20_19,
            R.drawable.d20_20
        )
    )
}
