# Libraries
library(ggplot2)
library(matrixStats)
ta  <- as.matrix(read.table(file.choose(), sep=",", header = FALSE))
ta <- t(ta)
dim(ta)
head(ta)

# Create dummy data
data <- data.frame(
  y=ta[,1],
  y2=ta[,2],
  y3=ta[,3],
  y4=ta[,4],
  y5=ta[,5],
  y6=ta[,6],
  y7=ta[,7],
  y8=ta[,8],
  y9=ta[,9],
  y10=ta[,10],
  y11=ta[,11],
  y12=ta[,12],
  y13=ta[,13],
  y14=ta[,14],
  y15=ta[,15],
  x=seq(1, length(ta[,1]))
)
ggplot(data, aes(x*1000)) +
  labs(title="Discount factor = 0.99",
       x ="Timestamp", y = "Avg. reward per timestamp", color = "Learning rate") +
  ylim(-1.5,0.6) +
  geom_line(aes(y = y, colour = "0.1"), size=1)+
  geom_line(aes(y = y2, colour = "0.3"), size=1) +
  geom_line(aes(y = y3, colour = "0.5"), size=1) +
  geom_line(aes(y = y4, colour = "0.7"), size=1) +
  geom_line(aes(y = y5, colour = "0.9"), size=1)
  
ggplot(data, aes(x*1000)) +
  labs(title="Discount factor = 0.9",
       x ="Timestamp", y = "Avg. reward per timestamp", color = "Learning rate") +
  ylim(-1.5,0.6) +
  geom_line(aes(y = y6, colour = "0.1"), size=1) +
  geom_line(aes(y = y7, colour = "0.3"), size=1) +
  geom_line(aes(y = y8, colour = "0.5"), size=1) +
  geom_line(aes(y = y9, colour = "0.7"), size=1) +
  geom_line(aes(y = y10, colour = "0.9"), size=1)

ggplot(data, aes(x*1000) ) +
  labs(title="Discount factor = 0.5",
       x ="Timestamp", y = "Avg. reward per timestamp", color = "Learning rate") +
  ylim(-1.5,0.6) +
  geom_line(aes(y = y11, colour = "0.1"), size=1) +
  geom_line(aes(y = y12, colour = "0.3"), size=1) +
  geom_line(aes(y = y13, colour = "0.5"), size=1) +
  geom_line(aes(y = y14, colour = "0.7"), size=1) +
  geom_line(aes(y = y15, colour = "0.9"), size=1) 
  
 # scale_x_log10(limits=c(1,150) ) 
 # scale_y_log10( breaks=c(1,50,500,2500,25000), limits=c(1,25000) )

plot(ta, x=x*1000, log="x", type="o")

convergence <- read.csv(file.choose(), header=FALSE, row.names=1)

sds <- rowSds(sapply(convergence[,-1], `length<-`, max(lengths(convergence[,-1]))), na.rm=TRUE)
men <- rowMeans(sapply(convergence[,-1], `length<-`, max(lengths(convergence[,-1]))), na.rm=TRUE)
print(sds)

# create dummy data
data <- data.frame(
  names=rownames(convergence),
  means=men,
  sds=sds
)

ggplot(data) +
  geom_bar(aes(x=names, y=means), stat="identity", fill="skyblue", alpha=0.7) +
  geom_errorbar( aes(x=names, ymin=means-sds, ymax=means+sds), width=0.4, colour="orange", alpha=0.9, size=1.3) +
  geom_text(aes(label=as.integer(means), x =names, y=means), position=position_dodge(width=0.9), vjust=-0.25) +
  xlab("Epsilon") + ylab("avg. amount of episodes until convergence") 


ba <- barplot(names=rownames(convergence), height=men, ylim=c(0, max(men)*1.2), ylab = "avg. episodes until convergence", xlab = "epsilon value")
text(x = ba, y = men, label = as.integer(men), pos = 3, cex = 0.8, col = "red")
