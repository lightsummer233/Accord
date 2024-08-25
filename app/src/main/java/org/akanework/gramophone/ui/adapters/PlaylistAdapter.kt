/*
 *     Copyright (C) 2024 Akane Foundation
 *
 *     Gramophone is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Gramophone is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.akanework.gramophone.ui.adapters

import android.content.Context
import android.net.Uri
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import org.akanework.gramophone.R
import org.akanework.gramophone.logic.findBaseWrapperFragment
import org.akanework.gramophone.logic.utils.MediaStoreUtils
import org.akanework.gramophone.ui.fragments.GeneralSubFragment

/**
 * [PlaylistAdapter] is an adapter for displaying artists.
 */
class PlaylistAdapter(
    fragment: Fragment,
    playlistList: MutableLiveData<List<MediaStoreUtils.Playlist>>,
) : BaseAdapter<MediaStoreUtils.Playlist>
    (
    fragment,
    liveData = playlistList,
    sortHelper = PlaylistItemHelper(fragment.requireContext()),
    naturalOrderHelper = null,
    initialSortType = Sorter.Type.ByTitleAscending,
    pluralStr = R.plurals.items,
    ownsView = true,
    defaultLayoutType = LayoutType.GRID
) {

    override val defaultCover = R.drawable.ic_default_cover_playlist

    override fun virtualTitleOf(item: MediaStoreUtils.Playlist): String {
        return context.getString(
            when (item) {
                is MediaStoreUtils.RecentlyAdded -> {
                    R.string.recently_added
                }

                is MediaStoreUtils.ManuScript -> {
                    R.string.manuscript
                }

                else -> {
                    R.string.unknown_playlist
                }
            }
        )
    }

    override fun onClick(item: MediaStoreUtils.Playlist) {
        fragment!!.findBaseWrapperFragment()!!.replaceFragment(GeneralSubFragment()) {
            putInt("Position", toRawPos(item))
            putInt("Item", R.id.playlist)
        }
    }

    override fun onMenu(item: MediaStoreUtils.Playlist, popupMenu: PopupMenu) {
        popupMenu.inflate(R.menu.more_menu_less)

        popupMenu.setOnMenuItemClickListener { it1 ->
            when (it1.itemId) {
                R.id.play_next -> {
                    val mediaController = mainActivity.getPlayer()
                    mediaController?.addMediaItems(
                        mediaController.currentMediaItemIndex + 1,
                        item.songList,
                    )
                    true
                }

                else -> false
            }
        }
    }

    class PlaylistItemHelper(private val context: Context) :
        StoreItemHelper<MediaStoreUtils.Playlist>() {
            /*
        override fun getCover(item: MediaStoreUtils.Playlist): Uri? {
            // return if (item is MediaStoreUtils.ManuScript) {
            //    context.resourceUri(R.drawable.ic_default_cover_manuscript)
            //} else {
            return super.getCover(item)
            //}
        }
             */
    }

}
