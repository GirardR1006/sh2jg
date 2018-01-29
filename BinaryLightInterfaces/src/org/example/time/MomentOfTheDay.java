    package org.example.time;
     
    public enum MomentOfTheDay {
        MORNING(6), AFTERNOON(12), EVENING(18), NIGHT(24);
     
        /**
         * Gets the moment of the day corresponding to the hour.
         * 
         * @param hour
         *            the given hour
         * @return the corresponding moment of the day
         */
        public static MomentOfTheDay getCorrespondingMoment(int hour) {
            assert ((0 <= hour) && (hour <= 24));
            MomentOfTheDay moment = MomentOfTheDay.NIGHT;
            
            if(hour<6){
            	moment=MomentOfTheDay.NIGHT;
            	}
            else if(hour<12){
            	moment=MomentOfTheDay.MORNING;
            }
            else if(hour<18){
            	moment=MomentOfTheDay.AFTERNOON;
            }
            else{
            	moment=MomentOfTheDay.NIGHT;
            }
            return moment;
        }
     
        /**
         * The hour when the moment start.
         */
        private int startHour=0;
     
        /**
         * Build a new moment of the day :
         * 
         * @param startHour
         *            when the moment start.
         */
        MomentOfTheDay(int startHour) {
            assert ((0 <= startHour) && (startHour <= 24));
            this.startHour = startHour;
        }
    }