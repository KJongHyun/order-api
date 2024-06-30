package com.kakaocafe.order.application.port.`in`

import com.kakaocafe.order.application.port.QuitMemberCommand

interface QuitMemberUseCase {
    fun quit(quitMemberCommand: QuitMemberCommand)
}