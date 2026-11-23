package com.junkfood.seal.util

/**
 * Heuristic check whether [this] looks like a link to a whole playlist/album/set rather than a
 * single video. Used to preselect the "Playlist" download type automatically.
 */
fun String.looksLikePlaylistUrl(): Boolean {
    val url = trim().lowercase()
    if (url.isEmpty()) return false
    val query = url.substringAfter('?', "").substringBefore('#')
    val params = query.split('&')
    val hasList = params.any { it.startsWith("list=") && it.length > "list=".length }
    val hasVideoId = params.any { it.startsWith("v=") }
    return when {
        // YouTube / YouTube Music: /playlist?list=..., or ?list=... without a specific video
        "/playlist" in url -> true
        hasList && !hasVideoId && "youtu" in url -> true
        // SoundCloud sets, Bandcamp / Spotify-like albums, Vimeo showcases etc.
        "soundcloud.com" in url && "/sets/" in url -> true
        "/album/" in url || "/albums/" in url -> true
        "vimeo.com" in url && "/showcase/" in url -> true
        else -> false
    }
}
