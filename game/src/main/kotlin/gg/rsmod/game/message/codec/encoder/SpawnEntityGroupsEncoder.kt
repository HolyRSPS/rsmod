package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SpawnEntityGroupsMessage
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SpawnEntityGroupsEncoder : MessageEncoder<SpawnEntityGroupsMessage>() {

    override fun extract(message: SpawnEntityGroupsMessage, key: String): Number = when (key) {
        "x" -> message.x
        "z" -> message.z
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SpawnEntityGroupsMessage, key: String): ByteArray = when (key) {
        "payload" -> {
            val builder = GamePacketBuilder()
            message.messages.forEach { groupMessage ->
                val encoder = message.encoders.get(groupMessage.message::class.java)!!
                val structure = message.structures.get(groupMessage.message::class.java)!!
                builder.put(DataType.BYTE, groupMessage.id) // Client always read as unsigned byte
                encoder.encode(groupMessage.message, builder, structure)
            }
            val payload = ByteArray(builder.getBuffer().readableBytes())
            builder.getBuffer().readBytes(payload)
            payload
        }
        else -> throw Exception("Unhandled value key.")
    }

}