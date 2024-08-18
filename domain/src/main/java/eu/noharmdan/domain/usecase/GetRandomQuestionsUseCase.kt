package eu.noharmdan.domain.usecase

import eu.noharmdan.common.base.UseCase
import eu.noharmdan.data.model.Question
import eu.noharmdan.data.model.QuestionCategory
import eu.noharmdan.data.model.QuestionDifficulty
import eu.noharmdan.domain.rest.ApiResponse
import eu.noharmdan.domain.rest.RestClient
import eu.noharmdan.domain.rest.getResult
import kotlinx.coroutines.flow.flow

class GetRandomQuestionsUseCase(
    private val restClient: RestClient
) : UseCase<GetRandomQuestionsUseCase.GetRandomQuestionsParams, List<Question>?>() {

    override fun execute(params: GetRandomQuestionsParams) = flow {
        val response = restClient.getResult {
            getRandomQuestions(
                limit = params.limit,
                categories = params.categories,
                difficulties = params.difficulties,
                tags = params.tags
            )
        }

        emit(
            when (response) {
                is ApiResponse.Data -> response.data.map { questionResponse ->
                    questionResponse.toQuestion()
                }
                is ApiResponse.NoContent -> emptyList()
                is ApiResponse.Error -> {
                    response.error?.printStackTrace()
                    null
                }
                else -> null
            }
        )
    }

    data class GetRandomQuestionsParams(
        val limit: Int? = null,
        val categories: List<QuestionCategory>? = null,
        val difficulties: List<QuestionDifficulty>? = null,
        val tags: List<String>? = null,
    ) : Params
}