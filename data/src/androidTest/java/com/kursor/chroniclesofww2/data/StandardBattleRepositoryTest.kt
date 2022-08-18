package com.kursor.chroniclesofww2.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kursor.chroniclesofww2.data.repositories.battle.StandardBattleRepositoryImpl
import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.model.serializable.Battle
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StandardBattleRepositoryTest {

    val context = InstrumentationRegistry.getInstrumentation().targetContext


    @Test
    fun getStandardScenarioDataList() {
        val repo = StandardBattleRepositoryImpl(context)
        val expected = listOf(
            Battle.Data(
                0, Nation.BRITAIN, mapOf(
                    Division.Type.INFANTRY to 6,
                    Division.Type.ARMORED to 3,
                    Division.Type.ARTILLERY to 2
                ),
                Nation.GERMANY, mapOf(
                    Division.Type.INFANTRY to 6,
                    Division.Type.ARMORED to 3,
                    Division.Type.ARTILLERY to 2
                )
            ),
            Battle.Data(
                1, Nation.GERMANY, mapOf(
                    Division.Type.INFANTRY to 6,
                    Division.Type.ARMORED to 4,
                    Division.Type.ARTILLERY to 2
                ),
                Nation.FRANCE, mapOf(
                    Division.Type.INFANTRY to 8,
                    Division.Type.ARMORED to 2,
                    Division.Type.ARTILLERY to 4
                )
            ),
            Battle.Data(
                2, Nation.GERMANY, mapOf(
                    Division.Type.INFANTRY to 6,
                    Division.Type.ARMORED to 4,
                    Division.Type.ARTILLERY to 4
                ),
                Nation.USSR, mapOf(
                    Division.Type.INFANTRY to 8,
                    Division.Type.ARMORED to 2,
                    Division.Type.ARTILLERY to 4
                )
            ),
            Battle.Data(
                3, Nation.JAPAN, mapOf(
                    Division.Type.INFANTRY to 10,
                    Division.Type.ARMORED to 0,
                    Division.Type.ARTILLERY to 0
                ),
                Nation.USA, mapOf(
                    Division.Type.INFANTRY to 6,
                    Division.Type.ARMORED to 0,
                    Division.Type.ARTILLERY to 4
                )
            ),
            Battle.Data(
                4, Nation.ITALY, mapOf(
                    Division.Type.INFANTRY to 6,
                    Division.Type.ARMORED to 4,
                    Division.Type.ARTILLERY to 4
                ),
                Nation.BRITAIN, mapOf(
                    Division.Type.INFANTRY to 8,
                    Division.Type.ARMORED to 2,
                    Division.Type.ARTILLERY to 4
                )
            ),
            Battle.Data(
                5, Nation.GERMANY, mapOf(
                    Division.Type.INFANTRY to 6,
                    Division.Type.ARMORED to 4,
                    Division.Type.ARTILLERY to 4
                ),
                Nation.USA, mapOf(
                    Division.Type.INFANTRY to 8,
                    Division.Type.ARMORED to 2,
                    Division.Type.ARTILLERY to 4
                )
            ),
        )
        assertEquals(expected, repo.battleList)
    }


}