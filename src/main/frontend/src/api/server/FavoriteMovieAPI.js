import axios from "axios";
import queryString from "query-string";


// Set or delete a movie from the favorite list
export const postFavoriteMovie = async (idMovie) => {
    try {
        const params = {
            idMovie: idMovie
        };

        const response = await axios.post(
            `http://localhost:8080/movies/favorite/set`,
            queryString.stringify(params),
            { withCredentials: true },
        );

        return response.data;
    } catch (err) {
        console.log(err);
    }
};

// Check if a user has favorite a movie
export const getFavoriteMovie = async (idMovie) => {
    try {
        const options = {
            withCredentials: true
        }
        const response = await axios.get(
            `http://localhost:8080/movies/favorite/get?idMovie=${idMovie}`,
            options
        );

        return response.data;
    } catch (err) {
        console.log(err);
    }
}

// getAllCountFavoriteByIdMovie
export const getFavoriteCountMovie = async (idMovie) => {
    try {
        const options = {
            withCredentials: true
        }
        const response = await axios.get(
            `http://localhost:8080/movies/favorite/count/favorite?id=${idMovie}`,
            options
        );

        return response.data;
    } catch (err) {
        console.log(err);
    }
}

// Get all movies user's favorite list
export const getAllFavoriteMovie = async () => {
    try {
        const options = {
            withCredentials: true
        }
        const response = await axios.get(
            `http://localhost:8080/movies/favorite/all`,
            options
        );

        return response.data;
    } catch (err) {
        console.log(err);
    }
}