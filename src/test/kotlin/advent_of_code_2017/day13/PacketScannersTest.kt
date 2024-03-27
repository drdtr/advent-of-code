package advent_of_code_2017.day13

import advent_of_code_2017.day13.PacketScanners.*
import advent_of_code_2017.day13.PacketScannersTest.ArgumentsProviderPacketScanners.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.math.min

class PacketScannersTest {
    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderTripSeverity::class)
    fun `test calcTripSeverity`(expected: Int, firewall: Firewall) = with(PacketScanners()) {
        assertEquals(expected, calcTripSeverity(firewall))
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderDelayWithoutBeingCaught::class)
    fun `test minDelayWithoutBeingCaught`(expected: Int, firewall: Firewall) = with(PacketScanners()) {
        assertEquals(expected, minDelayWithoutBeingCaught(firewall))
    }

    private interface ArgumentsProviderPacketScanners : ArgumentsProvider {
        companion object {
            val firewall1 = mapOf(0 to 3, 1 to 2, 4 to 4, 6 to 4)
        }

        class ArgumentsProviderTripSeverity : ArgumentsProviderPacketScanners {
            override fun provideArguments(context: ExtensionContext?): Stream<Arguments> {
                return Stream.of(
                    Arguments.of(24, firewall1),
                )
            }
        }

        class ArgumentsProviderDelayWithoutBeingCaught : ArgumentsProviderPacketScanners {
            override fun provideArguments(context: ExtensionContext?): Stream<Arguments> {
                return Stream.of(
                    Arguments.of(10, firewall1),
                )
            }
        }
    }
}