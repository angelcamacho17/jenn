package com.example.server.util;

import com.example.server.exception.ResourceNotFoundException;
import com.example.server.model.*;
import com.example.server.payload.*;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelMapper {
    
    @Autowired
    private static UserRepository userRepository;
    
    public static PollResponse mapPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, User creator,Long currentUserId, Long userVote) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setQuestion(poll.getQuestion());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(false);
        
        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());
            choiceResponse.setUserId(currentUserId);
            
            if(choiceVotesMap.containsKey(choice.getId())) {
                choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
                choiceResponse.setCorrect(choice.getCorrect());
            } else {
                choiceResponse.setVoteCount(0);
                choiceResponse.setCorrect(choice.getCorrect());
            }
            return choiceResponse;
        }).collect(Collectors.toList());
        
        pollResponse.setChoices(choiceResponses);
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName(), creator.getRoles());
        pollResponse.setCreatedBy(creatorSummary);
        
        if(userVote != null) {
            pollResponse.setSelectedChoice(userVote);
        }
        
        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(totalVotes);
        
        return pollResponse;
    }
    public static ThoughtResponse mapThoughtToThoughtResponse(Thought thought, User creator) {
        ThoughtResponse thoughtResponse= new ThoughtResponse();
        thoughtResponse.setId(thought.getId());
        thoughtResponse.setMessage(thought.getMessage());
        thoughtResponse.setCreationDateTime(thought.getCreatedAt());
    
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName(), creator.getRoles());
        thoughtResponse.setCreatedBy(creatorSummary);
        
        return thoughtResponse;
    }

    public static GameResponse mapGameToGameResponse(Game game, User creator) {
        GameResponse gameResponse= new GameResponse();
        gameResponse.setId(game.getId());
        gameResponse.setTitle(game.getTitle());
        gameResponse.setNumberPolls(game.getNumberPolls());

        Instant today = Instant.now();
        Instant later = game.getStartDate().plus(Duration.ofMinutes(2));

        gameResponse.setIsAvailable(today.isAfter(game.getStartDate()) && today.isBefore(later));
        gameResponse.setFinished(today.isAfter(later));
        gameResponse.setStartDate(game.getStartDate());

        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName(),creator.getRoles());
        gameResponse.setCreatedBy(creatorSummary);
        
        return gameResponse;
    }

    public static VoteResultResponse mapVoteResultToVoteResultResponse(VoteResult voteResult, User gameCreator) {
        
        VoteResultResponse voteResponse= new VoteResultResponse();
        voteResponse.setId(voteResult.getId());
        voteResponse.setGame(voteResult.getGame());
        voteResponse.setGameCreator(gameCreator);
        voteResponse.setExecutionTime(voteResult.getExecutionTime());
        voteResponse.setUser(voteResult.getUser());
        voteResponse.setTotalHits(voteResult.getTotalHits());
        
        return voteResponse;
    }
    
}