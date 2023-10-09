package furhatos.app.openaichat.setting

import furhatos.app.openaichat.flow.chatbot.OpenAIChatbot
import furhatos.flow.kotlin.FlowControlRunner
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.voice.AcapelaVoice
import furhatos.flow.kotlin.voice.PollyNeuralVoice
import furhatos.flow.kotlin.voice.Voice
import furhatos.nlu.SimpleIntent

class Persona(
    val name: String,
    val otherNames: List<String> = listOf(),
    val intro: String = "",
    val desc: String,
    val face: List<String>,
    val mask: String = "adult",
    val voice: List<Voice>
) {
    val fullDesc = "$name, the $desc"

    val intent = SimpleIntent((listOf(name, desc, fullDesc) + otherNames))

    /** The prompt for the openAI language model **/
    val chatbot = OpenAIChatbot(
        "The following is a quiz session between $name, the $desc, and a Person. Both will take turns asking and answering questions. Let's keep a score count for both $name and the Person to see who gets the most correct answers.",
        "Person",
        name
    )

}

fun FlowControlRunner.activate(persona: Persona) {
    for (voice in persona.voice) {
        if (voice.isAvailable) {
            furhat.voice = voice
            break
        }
    }

    for (face in persona.face) {
        if (furhat.faces[persona.mask]?.contains(face)!!) {
            furhat.character = face
            break
        }
    }
}

val hostPersona = Persona(
    name = "Host",
    desc = "host",
    face = listOf("Ivan", "default"),
    voice = listOf(PollyNeuralVoice("Matthew"))
)

val personas = listOf(
    Persona(
        name = "Isaac Newton",
        desc = "famous mathematician, and physicist",
        intro = "What you want to know?",
        face = listOf("Marty"),
        voice = listOf(PollyNeuralVoice("Brian"))
    ),
    Persona(
        name = "Marie Curie",
        desc = "well-known chemist, and physicist",
        intro = "Do you have any questions related to chemistry or physics?",
        face = listOf("Fedora"),
        voice = listOf(PollyNeuralVoice("Olivia"))
    ),
    Persona(
        name = "Charles Darwin",
        otherNames = listOf("Charles", "Darwin"),
        desc = "geologist and biologist",
        intro = "What can I help you with?",
        face = listOf("Marty"),
        voice = listOf(PollyNeuralVoice("Joey"))
    )
)