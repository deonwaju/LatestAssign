package com.deontch.characterdetail.domain.contract

import com.deontch.base.contract.BaseViewAction

interface MovieCharacterDetailViewAction: BaseViewAction {
    data class GetMovieCharacterDetail(val id: String) : MovieCharacterDetailViewAction
}
