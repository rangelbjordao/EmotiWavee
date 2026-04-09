package org.example.emotiwave.application.dto.in;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Generated;

public class MusicasUsuarioSpotifyDto {
    private List<Track> items;

    @Generated
    public MusicasUsuarioSpotifyDto() {
    }

    @Generated
    public List<Track> getItems() {
        return this.items;
    }

    @Generated
    public void setItems(final List<Track> items) {
        this.items = items;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof MusicasUsuarioSpotifyDto other)) {
            return false;
        } else {
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$items = this.getItems();
                Object other$items = other.getItems();
                if (this$items == null) {
                    return other$items == null;
                } else return this$items.equals(other$items);
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof MusicasUsuarioSpotifyDto;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $items = this.getItems();
        result = result * 59 + ($items == null ? 43 : $items.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "MusicasUsuarioSpotifyDto(items=" + this.getItems() + ")";
    }

    public static class Track {
        private String name;
        private List<Artist> artists;
        private String id;
        private String genero;
        private Album album;

        public String getArtistsNames() {
            return this.artists != null && !this.artists.isEmpty()
                    ? this.artists.stream().map(Artist::getName).collect(Collectors.joining(", "))
                    : "Desconhecido";
        }

        public String getArtistsIds() {
            return this.artists != null && !this.artists.isEmpty()
                    ? this.artists.stream().map(Artist::getId).collect(Collectors.joining(","))
                    : "";
        }

        public String getImagemUrl() {
            if (this.album != null && this.album.getImages() != null && !this.album.getImages().isEmpty()) {
                return this.album.getImages().get(0).getUrl();
            }
            return null;
        }

        @Generated
        public Track() {
        }

        @Generated
        public String getName() {
            return this.name;
        }

        @Generated
        public List<Artist> getArtists() {
            return this.artists;
        }

        @Generated
        public String getId() {
            return this.id;
        }

        @Generated
        public String getGenero() {
            return this.genero;
        }

        @Generated
        public Album getAlbum() {
            return this.album;
        }

        @Generated
        public void setName(final String name) {
            this.name = name;
        }

        @Generated
        public void setArtists(final List<Artist> artists) {
            this.artists = artists;
        }

        @Generated
        public void setId(final String id) {
            this.id = id;
        }

        @Generated
        public void setGenero(final String genero) {
            this.genero = genero;
        }

        @Generated
        public void setAlbum(final Album album) {
            this.album = album;
        }

        @Generated
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Track other)) {
                return false;
            } else {
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    Object this$name = this.getName();
                    Object other$name = other.getName();
                    if (this$name == null) {
                        if (other$name != null) {
                            return false;
                        }
                    } else if (!this$name.equals(other$name)) {
                        return false;
                    }

                    Object this$artists = this.getArtists();
                    Object other$artists = other.getArtists();
                    if (this$artists == null) {
                        if (other$artists != null) {
                            return false;
                        }
                    } else if (!this$artists.equals(other$artists)) {
                        return false;
                    }

                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id != null) {
                            return false;
                        }
                    } else if (!this$id.equals(other$id)) {
                        return false;
                    }

                    Object this$genero = this.getGenero();
                    Object other$genero = other.getGenero();
                    if (this$genero == null) {
                        if (other$genero != null) {
                            return false;
                        }
                    } else if (!this$genero.equals(other$genero)) {
                        return false;
                    }

                    Object this$album = this.getAlbum();
                    Object other$album = other.getAlbum();
                    if (this$album == null) {
                        return other$album == null;
                    } else return this$album.equals(other$album);
                }
            }
        }

        @Generated
        protected boolean canEqual(final Object other) {
            return other instanceof Track;
        }

        @Generated
        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Object $name = this.getName();
            result = result * 59 + ($name == null ? 43 : $name.hashCode());
            Object $artists = this.getArtists();
            result = result * 59 + ($artists == null ? 43 : $artists.hashCode());
            Object $id = this.getId();
            result = result * 59 + ($id == null ? 43 : $id.hashCode());
            Object $genero = this.getGenero();
            result = result * 59 + ($genero == null ? 43 : $genero.hashCode());
            Object $album = this.getAlbum();
            result = result * 59 + ($album == null ? 43 : $album.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            return "MusicasUsuarioSpotifyDto.Track(name=" + this.getName()
                    + ", artists=" + this.getArtists()
                    + ", id=" + this.getId()
                    + ", genero=" + this.getGenero()
                    + ", album=" + this.getAlbum() + ")";
        }

        public static class Artist {
            private String name;
            private String id;

            @Generated
            public Artist() {
            }

            @Generated
            public String getName() {
                return this.name;
            }

            @Generated
            public String getId() {
                return this.id;
            }

            @Generated
            public void setName(final String name) {
                this.name = name;
            }

            @Generated
            public void setId(final String id) {
                this.id = id;
            }

            @Generated
            public boolean equals(final Object o) {
                if (o == this) {
                    return true;
                } else if (!(o instanceof Artist other)) {
                    return false;
                } else {
                    if (!other.canEqual(this)) {
                        return false;
                    } else {
                        Object this$name = this.getName();
                        Object other$name = other.getName();
                        if (this$name == null) {
                            if (other$name != null) {
                                return false;
                            }
                        } else if (!this$name.equals(other$name)) {
                            return false;
                        }

                        Object this$id = this.getId();
                        Object other$id = other.getId();
                        if (this$id == null) {
                            return other$id == null;
                        } else return this$id.equals(other$id);
                    }
                }
            }

            @Generated
            protected boolean canEqual(final Object other) {
                return other instanceof Artist;
            }

            @Generated
            public int hashCode() {
                int PRIME = 59;
                int result = 1;
                Object $name = this.getName();
                result = result * 59 + ($name == null ? 43 : $name.hashCode());
                Object $id = this.getId();
                result = result * 59 + ($id == null ? 43 : $id.hashCode());
                return result;
            }

            @Generated
            public String toString() {
                return "MusicasUsuarioSpotifyDto.Track.Artist(name=" + this.getName()
                        + ", id=" + this.getId() + ")";
            }
        }

        public static class Album {
            private List<Image> images;

            public Album() {
            }

            public List<Image> getImages() {
                return images;
            }

            public void setImages(List<Image> images) {
                this.images = images;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Album album)) return false;
                return images != null ? images.equals(album.images) : album.images == null;
            }

            @Override
            public int hashCode() {
                return images != null ? images.hashCode() : 0;
            }

            @Override
            public String toString() {
                return "Album(images=" + images + ")";
            }
        }

        public static class Image {
            private String url;

            public Image() {
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Image image)) return false;
                return url != null ? url.equals(image.url) : image.url == null;
            }

            @Override
            public int hashCode() {
                return url != null ? url.hashCode() : 0;
            }

            @Override
            public String toString() {
                return "Image(url=" + url + ")";
            }
        }
    }
}