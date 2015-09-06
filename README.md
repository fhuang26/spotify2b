# spotify2b
Spotify Streamer, stage 2 : resubmit

(1) Music playing dialog fragment shows better, more complete view.
    This is item 1 in grader's list to do. Mainly we use maxHeight,
    maxWidth for ImageView in res/layout/dialog_fragment_player.xml ,
    and Picasso to resize album art image.

(2) 2-pane GUI is supported for tablet. (item 2 in grader's list)
    We use layout-sw600dp to override layout for tablet, and
    menu-sw600dp to override menu under res. Roughly,
    phone : SpotifyActivity, ArtistFragment (artists) ---->
            TracksActivity, TrackFragment (tracks) ---->
            MediaPlayerDialogFragment (music player)
    tablet : SpotifyActivity has 2 panes, ArtistFragment and
            TrackFragment. TrackFragment may lauch playing dialog.

(3) Music stops immediately when some events occur such as system back
    button, next, prev, pause. (item 3 in grader's list) This is supported
    by thread termination. The following helps ensure threads are
    terminated properly in MediaPlayerDialogFragment.java .
      (a) runnable.terminate()
      (b) ArtistTracksC.set_playing(false)
      (c) td.join(300)
      (d) ArtistTracksC.progress = ArtistTracksC.duration + 1
      (e) listener on song completion
      
(4) Dragging SeekBar (progress bar) forward and backward is developed.
    (item 4 in grader's list) Media player will match the new location.
    Since we try to support various events, for each new event, we start
    a new thread for music player and progress bar. Rotations do not
    affect music playing. We try to ensure old, unused threads are
    terminated by actions in (3). We also do atomic operations on
    critical resource by using synchronized in Java.

(5) "Now Playing" button in action bar as right-pointing triangle is
    made. (item 5a in grader's list) After a user presses system back,
    he/she can click that solid triangle (like play) in action bar to
    re-start the song. It will resume from the previous point stopped.
    If song playing reaches the end and a user clicks system back, then
    this triangle, it will start playing the song from beginning.
    
(6) Play, pause, next, prev will send intent to service and the service
    will create PendingIntent and notifications. (item 6b in grader's list)
    Based on guideline from coach in office-hour, only 1 notification (latest)
    in kept. After a user clicks system back a few times to leave the app or
    some interrupt occurs, later the user can turn on the phone/tablet,
    pull down notification drawer, and click the notification. It will
    restore previous state of this app and replay last track the user visited.
    The notification displays "Spotify service", track name, and album art image.

(7) Sharing : from this app, a user can post favorite songs to Facebook
    and share them with friends. (item 6c in grader's list) A Sharing
    MenuItem is provided (vertical ... in action bar). The following
    supports this.
      (a) R.id.miTrackShareTrack in menu_tracks.xml
      (b) R.id.miSpotifyShareTrack in menu_spotify.xml
      (c) share_track_facebook(playingTrack)

(8) Country code selection and showing notification control to show
    notification on lock screen or not are provided. (item 6d in grader's
    list) These are done by two additional MenuItems. In my test, by
    selecting US (United States) or CA (Canada) as country code, we
    get two different lists of 10 popular tracks for the same artist.

Best regards,
Felix Huang
