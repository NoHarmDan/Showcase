package eu.noharmdan.showcase.scene.quiz

import eu.noharmdan.common.base.ViewCommand

/**
 * The sealed subclass of [ViewCommand] to be used with [QuizViewModel],
 * its subclasses representing one-time view model to UI commands.
 */
sealed class QuizViewCommand : ViewCommand {
}