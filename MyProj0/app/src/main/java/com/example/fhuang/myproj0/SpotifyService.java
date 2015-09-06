package com.example.fhuang.myproj0;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class SpotifyService extends IntentService {
    public static final String SPOTIFY_SERVICE_KEY = "SpotifyService";
    public static final int SPOTIFY_NOTIFICATION_ID = 0;

    public SpotifyService() {
        super("SpotifyService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String val = intent.getStringExtra(SPOTIFY_SERVICE_KEY);
        Log.d(SPOTIFY_SERVICE_KEY, val);
        String msg = "";
        if (val.equals("next")) {
            msg = "Next : ";
        } else if (val.equals("prev")) {
            msg = "Previous : ";
        } else if (val.equals("play")) {
            msg = "Play : ";
        } else if (val.equals("pause")) {
            msg = "Pause : ";
        }
        int pos = ArtistTracksC.currPos;
        msg = msg + ArtistTracksC.ltATrack.get(pos).name;
        String album_image_url = ArtistTracksC.ltATrack.get(pos).album.imageUrl;
        sendNotification(msg, album_image_url, ArtistTracksC.ltATrack.get(pos).preview_url);
    }
    public NotificationCompat.Builder noteB = null;

    private void sendNotification(String msg, String album_image_url, String track_preview_url) {
        NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mgr.cancel(NOTIFICATION_SERVICE, SPOTIFY_NOTIFICATION_ID);
        // mgr.cancelAll();

        noteB = new NotificationCompat.Builder(this);

        // By default VISIBILITY_PUBLIC, notifications will be displayed on lock screen.
        // If a user picks VISIBILITY_PRIVATE by menu "notification visibility",
        // notifications will not appear on lock screen, and they are in drawer.
        //
        // noteB.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        // noteB.setVisibility(NotificationCompat.VISIBILITY_SECRET);
        noteB.setVisibility(ArtistPhotosC.notification_visibility);

        noteB.setContentTitle("Spotify Service");
        noteB.setContentText(msg);
        int icon = R.drawable.ic_launcher;
        noteB.setSmallIcon(icon);

        if (ArtistTracksC.bm_album != null) {
            noteB.setLargeIcon(ArtistTracksC.bm_album );
        }
        noteB.setAutoCancel(true);

        Intent i = new Intent(this, SpotifyActivity.class);
        i.putExtra("ArtistPhotosC_name_to_search", ArtistPhotosC.name_to_search);
        i.putExtra("ArtistTracksC_artistId", ArtistTracksC.artistId);
        i.putExtra("ArtistTracksC_track_preview_url", track_preview_url);
        /*
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(TracksActivity.class);
        stackBuilder.addNextIntent(i);
        PendingIntent pi = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        */
        PendingIntent pi =
                PendingIntent.getActivity(
                        this,
                        0,
                        i,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        noteB.setContentIntent(pi);

        Notification note = noteB.build();

        mgr.notify(SPOTIFY_NOTIFICATION_ID, note);
    }

    static public class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
