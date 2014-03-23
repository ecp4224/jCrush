package jcrush.model;

import jcrush.JCrush;
import jcrush.io.ConnectionType;
import jcrush.io.Requester;
import jcrush.system.ErrorHandler;
import jcrush.system.exceptions.FileUploadFailedException;

import javax.naming.OperationNotSupportedException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * A MediaCrush album
 *
 * @since API v2
 */
public class Album {
    protected List<MediaCrushFile> files = new ArrayList<MediaCrushFile>();
    private String hash;


    /**
     * Creates a new local album.
     * @since API v2
     */
    public Album() { }

    /**
     * Create a new local album with the specified {@link jcrush.model.MediaCrushFile} objects added to it.
     * @param file The file(s) to add
     * @since API v2
     */
    public Album(MediaCrushFile... file) {
        this(new ArrayList<MediaCrushFile>(Arrays.asList(file)));
    }

    /**
     * Create a new local album with the specified {@link jcrush.model.MediaCrushFile} objects added to it.
     * @param files The file(s) to add
     * @since API v2
     */
    public Album(List<MediaCrushFile> files) {
        this.files.addAll(files);
    }

    /**
     * Whether or not this is a local album, that is, this album currently has no hash from MediaCrush.
     * @return true if this album has no hash
     * @since API v2
     */
    public boolean isAlbumLocal() {
        return hash == null;
    }

    /**
     * Whether or not this is album is read-only, that is, this album currently has a hash from MediaCrush.
     * @return true if this album has a hash
     * @since API v2
     */
    public boolean isReadOnly() {
        return hash != null;
    }

    /**
     * Get an read-only {@link java.util.List} of {@link jcrush.model.MediaCrushFile} objects in this {@link jcrush.model.Album} <br></br>
     * Attempts to modify the returned list, whether direct or via its iterator, result in an {@link java.lang.UnsupportedOperationException}. To add/remove files, please invoke {@link jcrush.model.Album#add} or {@link jcrush.model.Album#remove}
     * @return A read-only {@link java.util.List} of {@link jcrush.model.MediaCrushFile} objects
     * @since API v2
     */
    public List<MediaCrushFile> getFiles() {
        return Collections.unmodifiableList(files);
    }

    /**
     * Add a new {@link jcrush.model.MediaCrushFile} to this {@link Album}. <br></br>
     * <b>{@link jcrush.model.MediaCrushFile} objects can only be added if {@link jcrush.model.Album#isReadOnly} returns false</b>, that is, the
     * album is a local album and {@link Album#commit()} has not been called or this {@link jcrush.model.Album} was not received from MediaCrush via a search. Attempts to
     * modify a read-only album will result in an {@link java.lang.UnsupportedOperationException}.<br></br>
     * Otherwise, this method behaves the same way as {@link java.util.List#add(Object)}.
     * @param file The {@link jcrush.model.MediaCrushFile} object to add
     * @return true (as specified by {@link java.util.Collection#add(Object)})
     * @throws java.lang.UnsupportedOperationException If this album is read-only
     * @since API v2
     * @see java.util.List#add(Object)
     */
    public boolean add(MediaCrushFile file) {
        if (isReadOnly())
            throw new UnsupportedOperationException("Files cannot be added to a non-local/read-only Album!");
        return files.add(file);
    }

    /**
     * Add a new {@link jcrush.model.MediaCrushFile} to this {@link Album} via a hash {@link java.lang.String}. <br></br>
     * <b>{@link jcrush.model.MediaCrushFile} objects can only be added if {@link jcrush.model.Album#isReadOnly} returns false</b>, that is, the
     * album is a local album and {@link Album#commit()} has not been called or this {@link jcrush.model.Album} was not received from MediaCrush via a search. Attempts to
     * modify a read-only album will result in an {@link java.lang.UnsupportedOperationException}.<br></br>
     * Otherwise, a search of the hash provided is done on the calling thread by invoking {@link jcrush.JCrush#getFile(String)}. If a file is found, then {@link jcrush.model.Album#add(MediaCrushFile)} is invoked with the found file.
     * @param hash The hash as a {@link String} of the {@link jcrush.model.MediaCrushFile} object to search for and add
     * @return true (as specified by {@link java.util.Collection#add(Object)})
     * @throws java.lang.UnsupportedOperationException If this album is read-only
     * @throws java.io.IOException As specified in {@link jcrush.JCrush#getFile(String)}
     * @throws java.io.FileNotFoundException If the hash specified was not found
     * @since API v2
     * @see java.util.List#add(Object)
     * @see jcrush.JCrush#getFile(String)
     * @see jcrush.model.Album#add(MediaCrushFile)
     */
    public boolean add(String hash) throws IOException {
        if (isReadOnly())
            throw new UnsupportedOperationException("Files cannot be added to a non-local/read-only Album!");
        MediaCrushFile file = JCrush.getFile(hash);
        if (file == null)
            throw new FileNotFoundException();
        return add(file);
    }

    /**
     * Remove the specified {@link jcrush.model.MediaCrushFile} object from this {@link jcrush.model.Album} <br></br>
     * <b>{@link jcrush.model.MediaCrushFile} objects can only be removed if {@link jcrush.model.Album#isReadOnly} returns false</b>, that is, the
     * album is a local album and {@link Album#commit()} has not been called or this {@link jcrush.model.Album} was not received from MediaCrush via a search. Attempts to
     * modify a read-only album will result in an {@link java.lang.UnsupportedOperationException}.<br></br>
     * Otherwise, this method behaves the same way as {@link java.util.List#remove(Object)}.
     * @param file The file to remove
     * @return true if this {@link jcrush.model.Album} contained the specified element
     * @throws java.lang.UnsupportedOperationException If this album is read-only
     * @since API v2
     */
    public boolean remove(MediaCrushFile file) {
        if (isReadOnly())
            throw new UnsupportedOperationException("Files cannot be added to a non-local/read-only Album!");
        return files.remove(file);
    }

    /**
     * Search for the specified hash and remove the resulting {@link jcrush.model.MediaCrushFile} object from this {@link jcrush.model.Album}. <br></br>
     * <b>{@link jcrush.model.MediaCrushFile} objects can only be removed if {@link jcrush.model.Album#isReadOnly} returns false</b>, that is, the
     * album is a local album and {@link Album#commit()} has not been called or this {@link jcrush.model.Album} was not received from MediaCrush via a search. Attempts to
     * modify a read-only album will result in an {@link java.lang.UnsupportedOperationException}.<br></br>
     * Otherwise, this method behaves the same way as {@link java.util.List#remove(Object)}.
     * @param hash The hash as a {@link String} of the {@link jcrush.model.MediaCrushFile} object to search for and add
     * @return true if this {@link jcrush.model.Album} contained the specified element
     * @throws java.lang.UnsupportedOperationException If this album is read-only
     * @since API v2
     */
    public boolean remove(String hash) {
        if (isReadOnly())
            throw new UnsupportedOperationException("Files cannot be added to a non-local/read-only Album!");
        Iterator<MediaCrushFile> iterator = files.iterator();
        while (iterator.hasNext()) {
            MediaCrushFile file = iterator.next();
            if (file.getHash().equals(hash)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Get the hash for this album. This method will return null if {@link Album#isAlbumLocal()} returns true, that is, the current Album is
     * a local album and not on MediaCrush.
     * @return The hash for this album or null if {@link Album#commit} has not been called yet.
     * @since API v2
     */
    public String getHash() {
        return hash;
    }

    /**
     * Upload this {@link jcrush.model.Album} object to MediaCrush. <b>An album can only be committed once,</b> as calling commit multiple times
     * would create a new album each time. <br></br>
     * This method is synchronous, so the web request is made on the calling thread. If this is not desired, then please invoke {@link jcrush.model.Album#commitAsync}
     * @since API v2
     */
    public void commit() throws IOException {
        if (!isAlbumLocal())
            throw new UnsupportedOperationException("This album is not local!");
        String post = "";
        for (int i = 0; i < files.size(); i++) {
            MediaCrushFile file = files.get(i);
            if (file != null && file.getHash() != null && !file.getHash().equals("")) {
                post += file.getHash();
            }
            if (i + 1 < files.size())
                post += ",";
        }

        post = "list=" + post;

        URL uri = new URL(JCrush.getApiURL() + "album/create");
        Requester requester = new Requester(ConnectionType.POST, uri);
        requester.setRecieve(true);
        requester.setPostData(post);

        try {
            requester.connect(); //Connect
        } catch (IOException e) {
            int code = requester.getResponseCode();
            switch (code) {
                case 404:
                    throw new FileNotFoundException("At least one of the items int he list could not be found.");
                case 413:
                    throw new FileUploadFailedException("An attempt was made to create an album that was too large.", e);
                case 415:
                    throw new FileUploadFailedException("At least one of the items in the list os not a File.\n(Did you try to create an album that contains an album?)", e);
                default:
                    throw new IOException("The server responded with an unknown error code! (" + code + ")", e);
            }
        }

        String json = requester.getResponse();

        requester.disconnect();

        Map jsonMap = JCrush.getJsonParser().fromJson(json, Map.class);
        if (jsonMap.containsKey("hash")) {
            this.hash = (String)jsonMap.get("hash");
        } else {
            throw new FileUploadFailedException("MediaCrush did not return a valid response! (No hash found in response)");
        }
    }

    /**
     * Upload this {@link jcrush.model.Album} object to MediaCrush. <b>An album can only be committed once,</b> as calling commit multiple times
     * would create a new album each time. <br></br>
     * This method is asynchronous, so the web request is made in a separate thread from the calling thread. If this is not desired, then please invoke {@link Album#commit()}
     * @since API v2
     */
    public void commitAsync() {
        commitAsync(null, null);
    }

    /**
     * Upload this {@link jcrush.model.Album} object to MediaCrush. <b>An album can only be committed once,</b> as calling commit multiple times
     * would create a new album each time. <br></br>
     * This method is asynchronous, so the web request is made in a separate thread from the calling thread. If this is not desired, then please invoke {@link Album#commit()}  <br></br>
     * The {@link java.lang.Runnable} object provided will be invoked when the web request is completed.
     * @param completedCallback The {@link java.lang.Runnable} object to invoke when the web request is complete. You may also specify null for none.
     * @param errorCallback The {@link jcrush.system.ErrorHandler} object to invoke when the web request fails. You may also specify null for none.
     * @since API v2
     */
    public void commitAsync(final Runnable completedCallback, final ErrorHandler errorCallback) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    commit();
                } catch (Throwable e) {
                    if (errorCallback != null)
                        errorCallback.run(e);
                }
                if (completedCallback != null)
                    completedCallback.run();
            }
        }).start();
    }
}
