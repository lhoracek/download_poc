package cz.lhoracek.newrelictest;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.OkHttpDownloader;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String LOCAL_FILENAME = "LargeFile.zip";

    private static final String REMOTE_URL = "http://mirror.filearena.net/pub/speed/SpeedTest_1024MB.dat?_ga=1.160245501.1922176621.1487950581";

    Button startButton;
    TextView status;
    ProgressBar progress;
    DownloadManager downloadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = (TextView) findViewById(R.id.textView_status);
        startButton = (Button) findViewById(R.id.button);
        progress = (ProgressBar) findViewById(R.id.progress);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload();
            }
        });

        downloadManager = new com.coolerfall.download.DownloadManager.Builder()
                .context(getApplicationContext())
                .downloader(OkHttpDownloader.create())
                .build();
    }

    private void startDownload() {
        DownloadRequest request = new DownloadRequest.Builder()
                .destinationFilePath(getLocalUrl(LOCAL_FILENAME))
                .url(REMOTE_URL)
                .retryInterval(1, TimeUnit.SECONDS)
                .retryTime(Integer.MAX_VALUE)
                .downloadCallback(new DownloadCallback() {
                    @Override
                    public void onStart(int downloadId, long totalBytes) {
                        super.onStart(downloadId, totalBytes);
                        appendStatus("new download started");
                    }

                    @Override
                    public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
                        super.onProgress(downloadId, bytesWritten, totalBytes);
                        // does not get called until whole file is fetched
                        int progressInt = (int) (100 * ((float) bytesWritten / (float) totalBytes));
                        progress.setProgress(progressInt);
                        appendStatus("Download progress " + progressInt + "  - " + bytesWritten);
                    }

                    @Override
                    public void onSuccess(int downloadId, String filePath) {
                        super.onSuccess(downloadId, filePath);
                        appendStatus("Download finished");
                    }

                    @Override
                    public void onFailure(int downloadId, int statusCode, String errMsg) {
                        super.onFailure(downloadId, statusCode, errMsg);
                        appendStatus("Failure");
                    }

                    @Override
                    public void onRetry(int downloadId) {
                        super.onRetry(downloadId);
                        appendStatus("Retry");
                    }


                })
                .build();

        downloadManager.add(request);
        appendStatus("Added download " + REMOTE_URL);
        appendStatus("Download manager task size " + downloadManager.getTaskSize());
    }

    private void appendStatus(String text) {
        status.setText(text + "\n" + status.getText());
    }

    private String getLocalUrl(String filename) {
        return getExternalFilesDir(Environment.DIRECTORY_MOVIES) + File.separator + filename;
    }


}
