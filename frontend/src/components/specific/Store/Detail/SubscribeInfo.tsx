import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import Carousel from "./Carousel";

import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";

interface Sub {
  subscribeId: number;
  subscribeName: string;
  subscribePrice: number;
  subscribeDescription: string;
  subscribeBeforePrice: number;
  weeklyFood: WeeklyFood[];
}

interface SubInfoProps {
  sub: Sub;
}

const SubscribeInfo = ({ sub }: SubInfoProps) => {
  return (
    <Accordion sx={{ width: "100%" }}>
      <AccordionSummary
        expandIcon={<ExpandMoreIcon />}
        aria-controls="panel1-content"
        id="panel1-header"
      >
        <div className="flex flex-col gap-4">
          <p className="text-2xl font-bold">{sub.subscribeName}</p>
          <p className="text-base">{sub.subscribeDescription}</p>
        </div>
      </AccordionSummary>
      <AccordionDetails>
        <Carousel weeklyFood={sub.weeklyFood} />
      </AccordionDetails>
    </Accordion>
  );
};

export default SubscribeInfo;
