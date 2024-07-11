package service;

import dao.InterestDao;
import dto.InterestDto;
import entity.InterestEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InterestServiceTest {
        InterestService interestService;

        InterestDao interestDaoMock;

    @BeforeEach
        void setup(){

            // the class under test
            interestService = new InterestService();

            // creating mock objects
            interestDaoMock = mock(InterestDao.class);

            // interest service uses the mock objects previously created
            interestService.setInterestDao(interestDaoMock);

            // preparation
            InterestEntity iEnt = new InterestEntity();
            iEnt.setId(1);
            iEnt.setName("Ballet");

            Set<InterestEntity> interests = new HashSet<InterestEntity>();
            interests.add(iEnt);

            // define behaviour of interestDaoMock
            when(interestDaoMock.findInterestByID(1)).thenReturn(iEnt);
            when(interestDaoMock.findInterestByID(2)).thenReturn(null);
            when(interestDaoMock.interestList()).thenReturn(interests);
            doNothing().when(interestDaoMock).persist(isA(InterestEntity.class));
            doNothing().when(interestDaoMock).merge(isA(InterestEntity.class));

        }

        @Test
        void testGetInterestByID(){

            int interestId = 1;
            InterestEntity iEnt = new InterestEntity();
            iEnt.setId(interestId);
            iEnt.setName("Ballet");

            InterestEntity interestEntityResult =interestService.getInterestByID(interestId);
            //assert
            assertNotNull(interestEntityResult);
            assertEquals(interestEntityResult, iEnt);

            //verification
            verify(interestDaoMock,times(2)).findInterestByID(interestId);
        }

        @Test
        void testGetInterestByID_NullInterest(){

            int interestId = 2;

            assertNull(interestService.getInterestByID(interestId));

            verify(interestDaoMock,times(1)).findInterestByID(interestId);
        }


        @Test
        void testCreateNewInterest(){

            InterestDto iDto = new InterestDto();
            iDto.setId(1);
            iDto.setName("Ballet");

            interestService.createNewInterest(iDto);

            //verification
            verify(interestDaoMock,times(1)).persist(isA(InterestEntity.class));

        }

        @Test
        void testInactivateInterest(){

            int interestId = 1;

            interestService.inactivateInterest(interestId);

            //verification
            verify(interestDaoMock,times(2)).findInterestByID(interestId);
            verify(interestDaoMock,times(1)).merge(isA(InterestEntity.class));
        }

        /*@Test
        void testInactiveInterest_NullInterest(){
            int interestId = 2;

            interestService.inactivateInterest(interestId);

            //verification
            doThrow(new EntityNotFoundException()).when(interestService).inactivateInterest(interestId);
        }*/

        @Test
        void testActiveInterestDtoList(){
            Set<InterestEntity> iEntities = new HashSet<InterestEntity>();

            InterestEntity iEnt1 = new InterestEntity();
            iEnt1.setId(1);
            iEnt1.setName("Coding");

            InterestEntity iEnt2 = new InterestEntity();
            iEnt2.setId(2);
            iEnt2.setName("Gardening");

            iEntities.add(iEnt1);
            iEntities.add(iEnt2);

            //mocked inside this class because we want to return a specific response for this test
            when(interestDaoMock.activeInterestList()).thenReturn(iEntities);
            Set<InterestDto> interestDtoListResult = interestService.activeInterestDtoList();
            //assert
            assertNotNull(interestDtoListResult);
            assertEquals(2,interestDtoListResult.size());

            //verification
            verify(interestDaoMock,times(1)).activeInterestList();

        }

        @Test
        void testCheckInterestValidity(){
            String validInterest = "Ballet";

            InterestEntity iEnt = new InterestEntity();
            iEnt.setName(validInterest);

            assertFalse(interestService.checkInterestValidity(iEnt));

            verify(interestDaoMock,times(1)).interestList();
        }

        @Test
        void testCheckInterestValidity_NotValid(){
            String invalidInterest = "Crabs";

            InterestEntity iEnt = new InterestEntity();
            iEnt.setName(invalidInterest);

            assertTrue(interestService.checkInterestValidity(iEnt));

            verify(interestDaoMock,times(1)).interestList();

        }
}
