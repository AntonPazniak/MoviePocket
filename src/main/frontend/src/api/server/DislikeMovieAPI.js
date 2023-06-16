import axios from "axios";
import queryString from "query-string";


// Set or delete a movie from the disliked list
export const postDislikedMovie = async (idMovie) => {
    try {
        const params = {
            idMovie: idMovie
        };

        const response = await axios.post(
            `http://localhost:8080/movies/dislike/set`,
            queryString.stringify(params),
            { withCredentials: true },
        );

        return response.data;
    } catch (err) {
        console.log(err);
    }
};

// Check if a user has disliked a movie
export const getDislikedMovie = async (idMovie) => {
    try {
        const options = {
            withCredentials: true
        }
        const response = await axios.get(
            `http://localhost:8080/movies/dislike/get?idMovie=${idMovie}`,
            options
        );

        return response.data;
    } catch (err) {
        console.log(err);
    }
}

// getAllCountDislikedByIdMovie
export const getDislikedCountMovie = async (idMovie) => {
    try {
        const options = {
            withCredentials: true
        }
        const response = await axios.get(
            `http://localhost:8080/movies/dislike/count/dislike?id=${idMovie}`,
            options
        );

        return response.data;
    } catch (err) {
        console.log(err);
    }
}

// Get all movies disliked by a user
export const getAllDislikedMovie = async () => {
    try {
        const options = {
            withCredentials: true
        }
        const response = await axios.get(
            `http://localhost:8080/movies/dislike/all`,
            options
        );

        return response.data;
    } catch (err) {
        console.log(err);
    }
}