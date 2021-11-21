package com.piapps.flashcardpro.core.db

import com.piapps.flashcardpro.core.db.tables.*
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by abduaziz on 2019-09-27 at 22:48.
 */

@Singleton
class DatabaseService
@Inject constructor(private val boxStore: BoxStore) {

    private val setTable by lazy { boxStore.boxFor(SetDb::class.java) }
    private val cardTable by lazy { boxStore.boxFor(CardDb::class.java) }
    private val labelTable by lazy { boxStore.boxFor(LabelDb::class.java) }
    private val statsTable by lazy { boxStore.boxFor(Stats::class.java) }

    fun saveSet(set: SetDb) = setTable.put(set)
    fun saveSets(sets: List<SetDb>) = setTable.put(sets)
    fun getSet(id: Long): SetDb? = setTable.get(id)
    fun deleteSet(set: SetDb) = setTable.remove(set)
    fun deleteSets(list: List<SetDb>) = setTable.remove(list)

    fun getAllSets(): List<SetDb> {
        // todo: Objectbox 2.9.1 BUG: less or greater is not working on 32-bit env.
        // val query = setTable.query().equal(SetDb_.isTrash, false).greater(SetDb_.id, 0)
        val query = setTable.query().equal(SetDb_.isTrash, false).greater(SetDb_.id, 0)
        val list = query.build().find()
        query.close()
        list.removeAll {
            it.id <= 0
        }
        return list.sortedBy { it.order }
    }

    fun getArchiveSets(): List<SetDb> {
        // todo: Objectbox 2.9.1 BUG: less or greater is not working on 32-bit env.
        // val query = setTable.query().less(SetDb_.id, 0).greater(SetDb_.count, 0)
        val query = setTable.query().equal(SetDb_.isTrash, false)
        val list = query.build().find()
        query.close()
        list.removeAll {
            it.id > 0
        }
        return list.sortedBy { it.order }
    }

    fun getTrashSets(): List<SetDb> {
        val query = setTable.query().equal(SetDb_.isTrash, true)
        val list = query.build().find()
        query.close()
        return list.sortedBy { it.order }
    }

    fun getLabelSets(label: String): List<SetDb> {
        val query = setTable.query().contains(SetDb_.labels, label).greater(SetDb_.id, 0)
        val list = query.build().find()
        query.close()
        return list.sortedBy { it.order }
    }

    fun saveCard(card: CardDb) = cardTable.put(card)
    fun saveCards(cards: List<CardDb>) = cardTable.put(cards)
    fun getCard(id: Long): CardDb? = cardTable.get(id)
    fun deleteCard(card: CardDb) = cardTable.remove(card)
    fun deleteCards(cards: List<CardDb>) = cardTable.remove(cards)
    fun getCards(setId: Long): List<CardDb> {
        val query = cardTable.query().equal(CardDb_.setId, setId)
        val list = query.build().find()
        query.close()
        return list.sortedBy { it.order }
    }

    fun getCardsByRating(setId: Long): List<CardDb> {
        val query = cardTable.query().equal(CardDb_.setId, setId).sort { o1, o2 ->
            o1!!.id.compareTo(o2!!.id)
        }
        val list = query.build().find()
        query.close()
        return list.sortedBy { it.rating }
    }

    fun saveLabel(label: LabelDb) =
        labelTable.put(label) // throws UniqueViolationException for labels

    fun deleteLabel(label: LabelDb) = labelTable.remove(label)
    fun getLabels() = labelTable.all

    fun saveStats(stats: Stats) = statsTable.put(stats)
    fun getStats(setId: Long, from: Long, to: Long): List<Stats> {
        val query = statsTable.query()
            .equal(Stats_.setId, setId)
            .between(Stats_.id, from, to)
            .build()
        val list = query.find()
        query.close()
        return list
    }

    fun clearStats(setId: Long) {
        val query = statsTable.query().equal(Stats_.setId, setId).build()
        val list = query.find()
        statsTable.remove(list)
        query.close()
    }
}